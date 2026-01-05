package org.dromara.system.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.system.domain.ProStaff;


import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 员工档案视图对象 pro_staff
 *
 * @author Lion Li
 * @date 2025-11-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProStaff.class)
public class ProStaffVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 档案表id，标记唯一一条档案记录
     */
    /*@ExcelProperty(value = "档案id")*/
    private Long proStaffId;

    /**
     * 人员名字
     */
    @ExcelProperty(value = "员工姓名")
    private String name;

    /**
     * 员工英文名
     */
    @ExcelProperty(value = "员工英文名")
    private String englishName;

    /**
     * 人员身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String idCardNumber;

    /**
     * 人员手机号码
     */
    @ExcelProperty(value = "手机号码")
    private String phonenumber;

    /**
     * 人员备用手机号码，可为空
     */
    @ExcelProperty(value = "备用手机号码")
    private String phonenumber1;

    /**
     * 员工四位工号
     */
    @ExcelProperty(value = "员工号")
    private String employeeNumber;

    /**
     * 员工四位部门号
     */
    @ExcelProperty(value = "部门号")
    private String deptNumber;

    /**
     * 员工登录号
     */
    @ExcelProperty(value = "员工登录账号")
    private String loginNumber;

    /**
     * 员工性别
     */
    @ExcelProperty(value = "员工性别")
    private String sex;

    /**
     * 员工是否在职
     */
    @ExcelProperty(value = "在职状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "pro_employee_status")
    private String status;

    /**
     * 员工所属部门
     */
    @ExcelProperty(value="部门")
    private String deptName;

    /**
     * 员工所属部门层级
     */
    @ExcelProperty(value = "部门层级")
    private String deptLevel;


    /**
     * 部门id
     */
    //@ExcelProperty(value = "部门id")
    private Long deptId;

    /**
     * 员工的基础工资
     */
    @ExcelProperty(value = "基本工资")
    private BigDecimal basicSalary;

    /**
     * 员工实际发放工资
     */
    @ExcelProperty(value = "实发工资")
    private BigDecimal actualSalary;

    /**
     * 工资成本=实发工资+公司其余个人支出
     */
    @ExcelProperty(value = "工资成本")
    private BigDecimal salaryCost;

    /**
     * 针对多岗位员工，确认其主岗位
     */
    @ExcelProperty(value = "是否主身份", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "pro_main_post")
    private String isMainPost;


    /**
     * 岗位id
     */
    //@ExcelProperty(value = "岗位id")
    private Long postId;

    /**
     * 员工岗位
     */
    @ExcelProperty(value="员工岗位")
    private String postName;

    /**
     * 员工入职日期
     */
    @ExcelProperty(value = "入职日期")
    private Date hireDate;

    /**
     * 员工离职日期
     */
    @ExcelProperty(value = "离职日期")
    private Date dimissionDate;


    /**
     * 员工职位
     */
    @ExcelProperty(value = "员工职位", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "pro_position_list")
    private String position;

    /**
     * 员工是否为主管
     */
    @ExcelProperty(value = "是否为主管", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "pro_is_supervisor")
    private String isSupervisor;

    /**
     * 员工邮箱
     */
    @ExcelProperty(value = "邮箱")
    private String email;

    /**
     * 分机号-与钉钉分机号对应
     */
    @ExcelProperty(value = "分机号")
    private String extensionNumber;

    /**
     * 员工办公地点
     */
    @ExcelProperty(value = "办公地点")
    private String officeLocation;

    /**
     * 账户是否冻结，默认离职账号均为冻结
     */
    @ExcelProperty(value = "冻结状态")
    private String isFrozen;

}
