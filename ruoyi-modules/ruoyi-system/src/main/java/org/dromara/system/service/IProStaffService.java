package org.dromara.system.service;

import jakarta.validation.constraints.Size;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.ProStaff;
import org.dromara.system.domain.bo.ProStaffBo;
import org.dromara.system.domain.vo.ProStaffVo;


import java.util.Collection;
import java.util.List;

/**
 * 员工档案Service接口
 *
 * @author Lion Li
 * @date 2025-11-14
 */
public interface IProStaffService {

    /**
     * 查询员工档案
     *
     * @param proStaffId 主键
     * @return 员工档案
     */
    ProStaff queryById(Long proStaffId);

    /**
     * 分页查询员工档案列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工档案分页列表
     */
    TableDataInfo<ProStaffVo> queryPageList(ProStaffBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工档案列表
     *
     * @param bo 查询条件
     * @return 员工档案列表
     */
    List<ProStaffVo> queryList(ProStaffBo bo);


    ProStaff queryByPhoneNumAndDeptId(String idCardNumber, Long deptId, String phonenumber);
    /**
     * 修改员工档案
     *
     * @param bo 员工档案
     * @return 是否修改成功
     */
    R<Void> updateByBo(ProStaffBo bo);

    /**
     * 冻结员工档案
     * @param bo 员工档案
     * @return 冻结结果
     */
    Boolean freezeByBo(ProStaffBo bo);

    /**
     * 校验并批量删除员工档案信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    R<List<ProStaffVo>> searchList(String params);

    R<Void> unfreezeByBo(ProStaffBo bo);

    R<Void> saveByBo(ProStaffBo bo);

    R<Void> archiveByBo(ProStaffBo bo);

    R<Void> resign(ProStaffBo bo);

    R<Void> rescindResign(ProStaffBo bo);

    Long getDeptIdByDeptNumber(String deptNumber);

    Long getPostIdByDeptIdAndPostName(Long deptId, String postName);

    Object queryByIdCardNum(@Size(min = 0, max = 18, message = "身份证长度不能超过{max}个字符") String idCardNumber);
}
