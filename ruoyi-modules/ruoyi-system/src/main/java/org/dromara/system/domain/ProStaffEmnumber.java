package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

@Data
@TableName("pro_staff_emnumber")
@EqualsAndHashCode(callSuper = false)
public class ProStaffEmnumber extends TenantEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 号码表id
     */
    @TableId(value = "pro_staff_emnumber_id")
    private Long proStaffEmnumberId;

    /**
     * 员工身份证号
     */
    private String idCardNumber;

    /**
     * 员工四位号码
     */
    private String employeeNumber;

    private String phonenumber;

}
