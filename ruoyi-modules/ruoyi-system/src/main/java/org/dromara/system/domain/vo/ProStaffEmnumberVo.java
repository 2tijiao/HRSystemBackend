package org.dromara.system.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.system.domain.ProStaffEmnumber;


import java.io.Serial;
import java.io.Serializable;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProStaffEmnumber.class)
public class ProStaffEmnumberVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 号码表id
     */
    @ExcelProperty("员工号码表id")
    private Long proStaffEmnumberId;

    /**
     * 员工身份证号
     */
    @ExcelProperty("员工身份证号")
    private String idCardNumber;

    /**
     * 员工四位号码
     */
    @ExcelProperty("员工号码")
    private String employeeNumber;

    @ExcelProperty("员工手机号码")
    private String phonenumber;

}
