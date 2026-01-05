package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.ProStaff;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工档案业务对象 pro_staff
 *
 * @author Lion Li
 * @date 2025-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AutoMapper(target = ProStaff.class, reverseConvertGenerate = false)
public class ProStaffBo extends BaseEntity {

    /**
     * 档案表id，标记唯一一条档案记录
     */
    @EqualsAndHashCode.Exclude
    private Long proStaffId;


    /**
     * 人员名字
     */
    private String name;

    /**
     * 员工性别
     */
    private String sex;

    /**
     * 员工英文名
     */
    private String englishName;

    /**
     * 员工号
     */
    private String employeeNumber;

    /**
     * 部门号
     */
    private String deptNumber;

    /**
     * 登录号
     */
    private String loginNumber;

    /**
     * 人员身份证号
     */
    @Size(min = 0, max = 18, message = "身份证长度不能超过{max}个字符")
    private String idCardNumber;

    /**
     * 人员手机号码
     */
    @Size(min = 0, max = 11, message = "联系电话长度不能超过{max}个字符")
    private String phonenumber;

    /**
     * 人员备用手机号码，可为空
     */
    @Size(min = 0, max = 11, message = "联系电话长度不能超过{max}个字符")
    private String phonenumber1;

    /**
     * 员工是否在职
     */
    private String status;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 员工的基础工资
     */
    private BigDecimal basicSalary;

    /**
     * 员工实际发放工资
     */
    private BigDecimal actualSalary;

    /**
     * 工资成本=实发工资+公司其余个人支出
     */
    private BigDecimal salaryCost;

    /**
     * 针对多岗位员工，确认其主岗位
     */
    private String isMainPost;

    /**
     * 岗位id
     */
    private Long postId;

    /**
     * 员工入职日期
     */
    private Date hireDate;

    /**
     * 员工离职日期
     */
    private Date dimissionDate;

    /**
     * 员工职位
     */
    private String position;

    /**
     * 员工是否为主管
     */
    private String isSupervisor;

    /**
     * 员工邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过{max}个字符")
    private String email;

    /**
     * 分机号-与钉钉分机号对应
     */
    private String extensionNumber;

    /**
     * 员工办公地点
     */
    private String officeLocation;

    /**
     * 账户是否冻结，默认离职账号均为冻结
     */
    @EqualsAndHashCode.Exclude
    private String isFrozen;

}
