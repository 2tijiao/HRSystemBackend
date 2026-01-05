package org.dromara.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.ProStaff;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.vo.ProStaffVo;
import org.dromara.system.domain.vo.SysUserVo;


import java.util.List;

/**
 * 员工档案Mapper接口
 *
 * @author Lion Li
 * @date 2025-11-14
 */
public interface ProStaffMapper extends BaseMapperPlus<ProStaff, ProStaffVo> {

    default List<ProStaffVo> selectListByParam(String params){
        List<ProStaffVo> list = this.selectVoList(new LambdaQueryWrapper<ProStaff>()
            .eq(ProStaff::getEmployeeNumber, params));
        if (!list.isEmpty()) {
            return list;
        }
        list = this.selectVoList(new LambdaQueryWrapper<ProStaff>()
            .eq(ProStaff::getIdCardNumber, params));
        if (!list.isEmpty()) {
            return list;
        }
        list = this.selectVoList(new LambdaQueryWrapper<ProStaff>()
            .like(ProStaff::getName, params)
            .or()
            .like(ProStaff::getEnglishName, params));
        return list;
    }


    // 使用注解实现 updateAndGetNumber
    @Select("UPDATE pro_num SET number = number + 1 RETURNING number")
    Long updateAndGetNumber();


    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "create_by")
    })
    default Page<ProStaffVo> selectPageStaff(Page<ProStaff> build, LambdaQueryWrapper<ProStaff> lqw){
        return this.selectVoPage(build, lqw);
    }
}
