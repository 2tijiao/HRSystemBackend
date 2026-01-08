package org.dromara.system.domain.vo;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.ProStaffEmnumber;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:37+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class ProStaffEmnumberVoToProStaffEmnumberMapperImpl implements ProStaffEmnumberVoToProStaffEmnumberMapper {

    @Override
    public ProStaffEmnumber convert(ProStaffEmnumberVo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProStaffEmnumber proStaffEmnumber = new ProStaffEmnumber();

        proStaffEmnumber.setProStaffEmnumberId( arg0.getProStaffEmnumberId() );
        proStaffEmnumber.setIdCardNumber( arg0.getIdCardNumber() );
        proStaffEmnumber.setEmployeeNumber( arg0.getEmployeeNumber() );
        proStaffEmnumber.setPhonenumber( arg0.getPhonenumber() );

        return proStaffEmnumber;
    }

    @Override
    public ProStaffEmnumber convert(ProStaffEmnumberVo arg0, ProStaffEmnumber arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setProStaffEmnumberId( arg0.getProStaffEmnumberId() );
        arg1.setIdCardNumber( arg0.getIdCardNumber() );
        arg1.setEmployeeNumber( arg0.getEmployeeNumber() );
        arg1.setPhonenumber( arg0.getPhonenumber() );

        return arg1;
    }
}
