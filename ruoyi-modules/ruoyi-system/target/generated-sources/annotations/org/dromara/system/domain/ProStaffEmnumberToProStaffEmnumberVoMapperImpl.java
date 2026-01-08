package org.dromara.system.domain;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.vo.ProStaffEmnumberVo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:38+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class ProStaffEmnumberToProStaffEmnumberVoMapperImpl implements ProStaffEmnumberToProStaffEmnumberVoMapper {

    @Override
    public ProStaffEmnumberVo convert(ProStaffEmnumber arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProStaffEmnumberVo proStaffEmnumberVo = new ProStaffEmnumberVo();

        proStaffEmnumberVo.setProStaffEmnumberId( arg0.getProStaffEmnumberId() );
        proStaffEmnumberVo.setIdCardNumber( arg0.getIdCardNumber() );
        proStaffEmnumberVo.setEmployeeNumber( arg0.getEmployeeNumber() );
        proStaffEmnumberVo.setPhonenumber( arg0.getPhonenumber() );

        return proStaffEmnumberVo;
    }

    @Override
    public ProStaffEmnumberVo convert(ProStaffEmnumber arg0, ProStaffEmnumberVo arg1) {
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
