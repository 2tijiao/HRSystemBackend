package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.ProStaffEmnumber;


@Data
@EqualsAndHashCode(callSuper = false)
@AutoMapper(target = ProStaffEmnumber.class, reverseConvertGenerate = false)
public class ProStaffEmnumberBo extends BaseEntity {
    /**
     * 号码表id
     */
    private Long proStaffEmnumberId;

    /**
     * 员工身份证号
     */
    private String idCardNumber;

    /**
     * 员工四位号码
     */
    private String employeeNumber;

}
