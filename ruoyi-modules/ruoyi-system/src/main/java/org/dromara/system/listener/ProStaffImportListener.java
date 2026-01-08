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
            // ------------------ 基础校验开始 ------------------
            if (ObjectUtil.isEmpty(staffVo.getDeptNumber())) {
                throw new ServiceException("部门号不能为空");
            }
            // 当身份证号为空时，手机号码不能为空
            if (ObjectUtil.isEmpty(staffVo.getIdCardNumber()) && ObjectUtil.isEmpty(staffVo.getPhonenumber())) {
                throw new ServiceException("当身份证号为空时，手机号码不能为空");
            }
            // ------------------ 基础校验结束 ------------------

            // 1. 补全ID信息
            staffVo.setDeptId(this.staffService.getDeptIdByDeptNumber(staffVo.getDeptNumber()));
            staffVo.setPostId(this.staffService.getPostIdByDeptIdAndPostName(staffVo.getDeptId(), staffVo.getPostName()));

            // ------------------ 核心修改点开始 ------------------

            // 2. 调用新方法：优先查询正常的记录
            // 只有当该员工在库里全是冻结记录时，这里才会返回冻结对象
            // 如果有一条正常、一条冻结，这里返回的一定是正常对象
            ProStaff existingStaff = this.staffService.queryPrioritizingActive(
                staffVo.getIdCardNumber(),
                staffVo.getDeptId(),
                staffVo.getPhonenumber()
            );

            // 3. 判断状态
            boolean isStaffExistAndActive = false;

            if (ObjectUtil.isNotNull(existingStaff)) {
                // 检查获取到的记录状态
                if ("1".equals(existingStaff.getIsFrozen())) {
                    // 能够进入这里，说明 Service 层没找到“正常记录”
                    // 此时确实只有冻结记录 -> 视为不存在，准备重新导入(新增)
                    log.info("员工 {} 在部门 {} 中仅存在冻结状态记录，将作为新记录处理",
                        existingStaff.getName(), existingStaff.getDeptId());
                    existingStaff = null; // 置空，后续走新增逻辑
                } else {
                    // 取到的是正常记录 (即使库里还有一条冻结的，也被Service层过滤掉了)
                    isStaffExistAndActive = true;
                }
            }

            // ------------------ 核心修改点结束 ------------------

            // 4. 分支处理：新增 OR 更新
            if (ObjectUtil.isNull(existingStaff)) {
                // --- 场景：完全的新人，或者旧账号已冻结被视为新人 ---
                ProStaffBo staff = BeanUtil.toBean(staffVo, ProStaffBo.class);
                ValidatorUtils.validate(staff);

                staff.setCreateBy(operUserId);
                staff.setIsFrozen("0"); // 新增时强制为正常状态

                staffService.saveByBo(staff);

                successNum++;
                successMsg.append("<br/>").append(successNum).append("、员工 ").append(staff.getName())
                    .append("(").append(staff.getEmployeeNumber()).append(") 导入成功");

            } else if (isUpdateSupport) {
                // --- 场景：存在正常账号，且允许更新 ---
                // 此时 existingStaff 就是那条正常的记录，不会误更新冻结的那条
                Long staffId = existingStaff.getProStaffId();
                ProStaffBo staff = BeanUtil.toBean(staffVo, ProStaffBo.class);

                staff.setProStaffId(staffId); // 锁定更新正常的那条ID
                ValidatorUtils.validate(staff);
                staff.setUpdateBy(operUserId);

                staffService.updateByBo(staff);

                successNum++;
                successMsg.append("<br/>").append(successNum).append("、员工 ").append(staff.getName())
                    .append("(").append(staff.getEmployeeNumber()).append(") 更新成功");

            } else {
                // --- 场景：存在正常账号，但不支持更新 ---
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、员工 ")
                    .append(existingStaff.getName()).append("(").append(existingStaff.getEmployeeNumber())
                    .append(") 已存在且未冻结");
            }

        } catch (Exception e) {
            // 异常处理逻辑
            failureNum++;
            String employeeInfo = ObjectUtil.isNotEmpty(staffVo.getEmployeeNumber()) ?
                staffVo.getName() + "(" + staffVo.getEmployeeNumber() + ")" : staffVo.getName();

            String msg = "<br/>" + failureNum + "、员工 " + HtmlUtil.cleanHtmlTag(employeeInfo) + " 导入失败：";
            String message = e.getMessage();

            if (e instanceof ConstraintViolationException cvException) {
                message = StreamUtils.join(cvException.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
            }

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
