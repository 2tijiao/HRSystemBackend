package org.dromara.system.domain;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.vo.ProStaffVo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:39+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class ProStaffToProStaffVoMapperImpl implements ProStaffToProStaffVoMapper {

    @Override
    public ProStaffVo convert(ProStaff arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProStaffVo proStaffVo = new ProStaffVo();

        proStaffVo.setProStaffId( arg0.getProStaffId() );
        proStaffVo.setName( arg0.getName() );
        proStaffVo.setEnglishName( arg0.getEnglishName() );
        proStaffVo.setIdCardNumber( arg0.getIdCardNumber() );
        proStaffVo.setPhonenumber( arg0.getPhonenumber() );
        proStaffVo.setPhonenumber1( arg0.getPhonenumber1() );
        proStaffVo.setEmployeeNumber( arg0.getEmployeeNumber() );
        proStaffVo.setDeptNumber( arg0.getDeptNumber() );
        proStaffVo.setLoginNumber( arg0.getLoginNumber() );
        proStaffVo.setSex( arg0.getSex() );
        proStaffVo.setStatus( arg0.getStatus() );
        proStaffVo.setDeptLevel( arg0.getDeptLevel() );
        proStaffVo.setDeptId( arg0.getDeptId() );
        proStaffVo.setBasicSalary( arg0.getBasicSalary() );
        proStaffVo.setActualSalary( arg0.getActualSalary() );
        proStaffVo.setSalaryCost( arg0.getSalaryCost() );
        proStaffVo.setIsMainPost( arg0.getIsMainPost() );
        proStaffVo.setPostId( arg0.getPostId() );
        proStaffVo.setHireDate( arg0.getHireDate() );
        proStaffVo.setDimissionDate( arg0.getDimissionDate() );
        proStaffVo.setPosition( arg0.getPosition() );
        proStaffVo.setIsSupervisor( arg0.getIsSupervisor() );
        proStaffVo.setEmail( arg0.getEmail() );
        proStaffVo.setExtensionNumber( arg0.getExtensionNumber() );
        proStaffVo.setOfficeLocation( arg0.getOfficeLocation() );
        proStaffVo.setIsFrozen( arg0.getIsFrozen() );

        return proStaffVo;
    }

    @Override
    public ProStaffVo convert(ProStaff arg0, ProStaffVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setProStaffId( arg0.getProStaffId() );
        arg1.setName( arg0.getName() );
        arg1.setEnglishName( arg0.getEnglishName() );
        arg1.setIdCardNumber( arg0.getIdCardNumber() );
        arg1.setPhonenumber( arg0.getPhonenumber() );
        arg1.setPhonenumber1( arg0.getPhonenumber1() );
        arg1.setEmployeeNumber( arg0.getEmployeeNumber() );
        arg1.setDeptNumber( arg0.getDeptNumber() );
        arg1.setLoginNumber( arg0.getLoginNumber() );
        arg1.setSex( arg0.getSex() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setDeptLevel( arg0.getDeptLevel() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setBasicSalary( arg0.getBasicSalary() );
        arg1.setActualSalary( arg0.getActualSalary() );
        arg1.setSalaryCost( arg0.getSalaryCost() );
        arg1.setIsMainPost( arg0.getIsMainPost() );
        arg1.setPostId( arg0.getPostId() );
        arg1.setHireDate( arg0.getHireDate() );
        arg1.setDimissionDate( arg0.getDimissionDate() );
        arg1.setPosition( arg0.getPosition() );
        arg1.setIsSupervisor( arg0.getIsSupervisor() );
        arg1.setEmail( arg0.getEmail() );
        arg1.setExtensionNumber( arg0.getExtensionNumber() );
        arg1.setOfficeLocation( arg0.getOfficeLocation() );
        arg1.setIsFrozen( arg0.getIsFrozen() );

        return arg1;
    }
}
