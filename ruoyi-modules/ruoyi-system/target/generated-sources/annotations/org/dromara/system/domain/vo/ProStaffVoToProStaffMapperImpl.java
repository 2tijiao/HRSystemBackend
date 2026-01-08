package org.dromara.system.domain.vo;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.ProStaff;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:38+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class ProStaffVoToProStaffMapperImpl implements ProStaffVoToProStaffMapper {

    @Override
    public ProStaff convert(ProStaffVo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProStaff proStaff = new ProStaff();

        proStaff.setProStaffId( arg0.getProStaffId() );
        proStaff.setName( arg0.getName() );
        proStaff.setEnglishName( arg0.getEnglishName() );
        proStaff.setIdCardNumber( arg0.getIdCardNumber() );
        proStaff.setPhonenumber( arg0.getPhonenumber() );
        proStaff.setSex( arg0.getSex() );
        proStaff.setPhonenumber1( arg0.getPhonenumber1() );
        proStaff.setEmployeeNumber( arg0.getEmployeeNumber() );
        proStaff.setDeptNumber( arg0.getDeptNumber() );
        proStaff.setLoginNumber( arg0.getLoginNumber() );
        proStaff.setStatus( arg0.getStatus() );
        proStaff.setDeptLevel( arg0.getDeptLevel() );
        proStaff.setDeptId( arg0.getDeptId() );
        proStaff.setBasicSalary( arg0.getBasicSalary() );
        proStaff.setActualSalary( arg0.getActualSalary() );
        proStaff.setSalaryCost( arg0.getSalaryCost() );
        proStaff.setIsMainPost( arg0.getIsMainPost() );
        proStaff.setPostId( arg0.getPostId() );
        proStaff.setHireDate( arg0.getHireDate() );
        proStaff.setDimissionDate( arg0.getDimissionDate() );
        proStaff.setPosition( arg0.getPosition() );
        proStaff.setIsSupervisor( arg0.getIsSupervisor() );
        proStaff.setEmail( arg0.getEmail() );
        proStaff.setExtensionNumber( arg0.getExtensionNumber() );
        proStaff.setOfficeLocation( arg0.getOfficeLocation() );
        proStaff.setIsFrozen( arg0.getIsFrozen() );

        return proStaff;
    }

    @Override
    public ProStaff convert(ProStaffVo arg0, ProStaff arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setProStaffId( arg0.getProStaffId() );
        arg1.setName( arg0.getName() );
        arg1.setEnglishName( arg0.getEnglishName() );
        arg1.setIdCardNumber( arg0.getIdCardNumber() );
        arg1.setPhonenumber( arg0.getPhonenumber() );
        arg1.setSex( arg0.getSex() );
        arg1.setPhonenumber1( arg0.getPhonenumber1() );
        arg1.setEmployeeNumber( arg0.getEmployeeNumber() );
        arg1.setDeptNumber( arg0.getDeptNumber() );
        arg1.setLoginNumber( arg0.getLoginNumber() );
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
