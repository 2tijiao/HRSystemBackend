package org.dromara.system.domain.bo;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.dromara.system.domain.ProStaffEmnumber;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:39+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class ProStaffEmnumberBoToProStaffEmnumberMapperImpl implements ProStaffEmnumberBoToProStaffEmnumberMapper {

    @Override
    public ProStaffEmnumber convert(ProStaffEmnumberBo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProStaffEmnumber proStaffEmnumber = new ProStaffEmnumber();

        proStaffEmnumber.setSearchValue( arg0.getSearchValue() );
        proStaffEmnumber.setCreateDept( arg0.getCreateDept() );
        proStaffEmnumber.setCreateBy( arg0.getCreateBy() );
        proStaffEmnumber.setCreateTime( arg0.getCreateTime() );
        proStaffEmnumber.setUpdateBy( arg0.getUpdateBy() );
        proStaffEmnumber.setUpdateTime( arg0.getUpdateTime() );
        Map<String, Object> map = arg0.getParams();
        if ( map != null ) {
            proStaffEmnumber.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        proStaffEmnumber.setProStaffEmnumberId( arg0.getProStaffEmnumberId() );
        proStaffEmnumber.setIdCardNumber( arg0.getIdCardNumber() );
        proStaffEmnumber.setEmployeeNumber( arg0.getEmployeeNumber() );
        proStaffEmnumber.setPhonenumber( arg0.getPhonenumber() );

        return proStaffEmnumber;
    }

    @Override
    public ProStaffEmnumber convert(ProStaffEmnumberBo arg0, ProStaffEmnumber arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setSearchValue( arg0.getSearchValue() );
        arg1.setCreateDept( arg0.getCreateDept() );
        arg1.setCreateBy( arg0.getCreateBy() );
        arg1.setCreateTime( arg0.getCreateTime() );
        arg1.setUpdateBy( arg0.getUpdateBy() );
        arg1.setUpdateTime( arg0.getUpdateTime() );
        if ( arg1.getParams() != null ) {
            Map<String, Object> map = arg0.getParams();
            if ( map != null ) {
                arg1.getParams().clear();
                arg1.getParams().putAll( map );
            }
            else {
                arg1.setParams( null );
            }
        }
        else {
            Map<String, Object> map = arg0.getParams();
            if ( map != null ) {
                arg1.setParams( new LinkedHashMap<String, Object>( map ) );
            }
        }
        arg1.setProStaffEmnumberId( arg0.getProStaffEmnumberId() );
        arg1.setIdCardNumber( arg0.getIdCardNumber() );
        arg1.setEmployeeNumber( arg0.getEmployeeNumber() );
        arg1.setPhonenumber( arg0.getPhonenumber() );

        return arg1;
    }
}
