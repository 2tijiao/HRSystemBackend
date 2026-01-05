package org.dromara.system.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.excel.core.ExcelListener;
import org.dromara.common.excel.core.ExcelResult;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.ProStaff;
import org.dromara.system.domain.bo.ProStaffBo;
import org.dromara.system.domain.vo.ProStaffVo;
import org.dromara.system.service.IProStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 员工档案自定义导入
 *
 * @author Lion Li
 */
@Slf4j
public class ProStaffImportListener extends AnalysisEventListener<ProStaffVo> implements ExcelListener<ProStaffVo> {

    private final IProStaffService staffService;

    private final Boolean isUpdateSupport;

    private final String operUserId;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public ProStaffImportListener(Boolean isUpdateSupport) {
        // 从Spring容器获取员工档案服务
        this.staffService = SpringUtils.getBean(IProStaffService.class);
        // 设置是否支持更新已存在记录
        this.isUpdateSupport = isUpdateSupport;
        // 获取当前操作者用户ID
        this.operUserId = LoginHelper.getUserId();
    }

    @Override
    public void invoke(ProStaffVo staffVo, AnalysisContext context) {
        try {
            // ------------------ 修改开始 ------------------
            // 1. 部门号依然是必须的
            if (ObjectUtil.isEmpty(staffVo.getDeptNumber())) {
                throw new ServiceException("部门号不能为空");
            }

            // 2. 修改逻辑：如果身份证号为空，则必须提供手机号码来确定一个人
            if (ObjectUtil.isEmpty(staffVo.getIdCardNumber()) && ObjectUtil.isEmpty(staffVo.getPhonenumber())) {
                throw new ServiceException("当身份证号为空时，手机号码不能为空");
            }
            // ------------------ 修改结束 ------------------

            staffVo.setDeptId(this.staffService.getDeptIdByDeptNumber(staffVo.getDeptNumber()));
            staffVo.setPostId(this.staffService.getPostIdByDeptIdAndPostName(staffVo.getDeptId(),staffVo.getPostName()));

            // 根据身份证号和部门ID查询已存在的员工档案
            // 注意：此处传入的 IdCardNumber 可能为空，Service层需支持根据手机号+部门ID查询的逻辑
            ProStaff existingStaff = this.staffService.queryByPhoneNumAndDeptId(
                staffVo.getIdCardNumber(),
                staffVo.getDeptId(),
                staffVo.getPhonenumber()
            );

            // 判断员工档案是否存在且是否为有效状态（非冻结状态）
            boolean isStaffExistAndActive = false;
            if (ObjectUtil.isNotNull(existingStaff)) {
                // 检查员工是否为冻结状态，"1"表示冻结
                if ("1".equals(existingStaff.getIsFrozen())) {
                    // 员工存在但处于冻结状态，视为不存在，可以重新导入
                    log.info("员工 {} 身份证号 {} 在部门 {} 中处于冻结状态，将作为新记录处理",
                        existingStaff.getName(), existingStaff.getIdCardNumber(), existingStaff.getDeptId());
                    existingStaff = null; // 设置为null，后续按新增处理
                } else {
                    // 员工存在且未冻结，视为有效存在
                    isStaffExistAndActive = true;
                }
            }

            if (ObjectUtil.isNull(existingStaff) || !isStaffExistAndActive) {
                // 员工不存在或处于冻结状态，执行新增操作
                ProStaffBo staff = BeanUtil.toBean(staffVo, ProStaffBo.class);
                // 验证数据格式和规则
                ValidatorUtils.validate(staff);
                // 设置创建者ID
                staff.setCreateBy(operUserId);
                // 新增时默认设置为未冻结状态（根据业务需求调整）
                staff.setIsFrozen("0");
                // 调用服务层插入数据
                staffService.saveByBo(staff);
                successNum++;
                // 记录成功信息
                successMsg.append("<br/>").append(successNum).append("、员工 ").append(staff.getName())
                    .append("(").append(staff.getEmployeeNumber()).append(") 导入成功");
            } else if (isUpdateSupport) {
                // 员工存在且支持更新，执行更新操作
                Long staffId = existingStaff.getProStaffId();
                ProStaffBo staff = BeanUtil.toBean(staffVo, ProStaffBo.class);
                // 设置要更新的记录ID
                staff.setProStaffId(staffId);
                // 验证数据格式和规则
                ValidatorUtils.validate(staff);

                // 设置更新者ID
                staff.setUpdateBy(operUserId);
                // 调用服务层更新数据
                staffService.updateByBo(staff);
                successNum++;
                // 记录成功信息
                successMsg.append("<br/>").append(successNum).append("、员工 ").append(staff.getName())
                    .append("(").append(staff.getEmployeeNumber()).append(") 更新成功");
            } else {
                // 员工已存在且不支持更新，记录失败信息
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、员工 ")
                    .append(existingStaff.getName()).append("(").append(existingStaff.getEmployeeNumber())
                    .append(") 已存在且未冻结");
            }
        } catch (Exception e) {
            // 处理导入过程中出现的异常
            failureNum++;
            // 构建员工标识信息，用于错误提示
            String employeeInfo = ObjectUtil.isNotEmpty(staffVo.getEmployeeNumber()) ?
                staffVo.getName() + "(" + staffVo.getEmployeeNumber() + ")" : staffVo.getName();
            // 清理HTML标签防止XSS攻击
            String msg = "<br/>" + failureNum + "、员工 " + HtmlUtil.cleanHtmlTag(employeeInfo) + " 导入失败：";
            String message = e.getMessage();
            // 处理数据验证异常，提取具体的验证错误信息
            if (e instanceof ConstraintViolationException cvException) {
                message = StreamUtils.join(cvException.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
            }
            // 记录错误信息
            failureMsg.append(msg).append(message);
            log.error(msg, e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 所有数据解析完成后的处理，记录导入统计信息
        log.info("员工档案导入完成，成功: {} 条，失败: {} 条", successNum, failureNum);
    }

    @Override
    public ExcelResult<ProStaffVo> getExcelResult() {
        return new ExcelResult<>() {

            @Override
            public String getAnalysis() {
                // 生成最终的导入结果分析报告
                if (failureNum > 0) {
                    // 如果有失败记录，抛出异常并显示详细错误信息
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    // 全部导入成功，显示成功信息
                    successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<ProStaffVo> getList() {
                // 返回导入的数据列表，这里返回null可根据需要调整
                return null;
            }

            @Override
            public List<String> getErrorList() {
                // 返回错误列表，这里返回null可根据需要调整
                return null;
            }
        };
    }
}
