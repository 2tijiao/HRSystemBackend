package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工档案对象 pro_staff
 *
 * @author Lion Li
 * @date 2025-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pro_staff")
public class ProStaff extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 档案表id，标记唯一一条档案记录
     */
    @TableId(value = "pro_staff_id")
    private Long proStaffId;

    /**
     * 人员名字
     */
    private String name;

    /**
     * 员工英文名
     */
    private String englishName;

    /**
     * 人员身份证号
     */
    private String idCardNumber;

    /**
     * 人员手机号码
     */
    private String phonenumber;

    /**
     *员工性别
     */
    private String sex;

    /**
     * 人员备用手机号码，可为空
     */
    private String phonenumber1;

    /**
     * 员工四位工号
     */
    private String employeeNumber;

    /**
     * 员工四位部门号
     */
    private String deptNumber;

    /**
     * 员工登录号
     */
    private String loginNumber;

    /**
     * 员工是否在职
     */
    private String status;

    /**
     * 员工所属部门层级
     */
    private String deptLevel;

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
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
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
    private String isFrozen;

}
