package org.dromara.system.domain.vo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.dromara.system.domain.SysDept;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-05T10:23:19+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class SysDeptVoToSysDeptMapperImpl implements SysDeptVoToSysDeptMapper {

    @Override
    public SysDept convert(SysDeptVo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysDept sysDept = new SysDept();

        sysDept.setCreateTime( arg0.getCreateTime() );
        sysDept.setDeptId( arg0.getDeptId() );
        sysDept.setDeptNumber( arg0.getDeptNumber() );
        sysDept.setParentId( arg0.getParentId() );
        sysDept.setDeptName( arg0.getDeptName() );
        sysDept.setDeptCategory( arg0.getDeptCategory() );
        sysDept.setOrderNum( arg0.getOrderNum() );
        if ( arg0.getLeader() != null ) {
            sysDept.setLeader( String.valueOf( arg0.getLeader() ) );
        }
        sysDept.setPhone( arg0.getPhone() );
        sysDept.setEmail( arg0.getEmail() );
        sysDept.setStatus( arg0.getStatus() );
        sysDept.setAncestors( arg0.getAncestors() );
        List<SysDept> list = arg0.getChildren();
        if ( list != null ) {
            sysDept.setChildren( new ArrayList<SysDept>( list ) );
        }

        return sysDept;
    }

    @Override
    public SysDept convert(SysDeptVo arg0, SysDept arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setCreateTime( arg0.getCreateTime() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setDeptNumber( arg0.getDeptNumber() );
        arg1.setParentId( arg0.getParentId() );
        arg1.setDeptName( arg0.getDeptName() );
        arg1.setDeptCategory( arg0.getDeptCategory() );
        arg1.setOrderNum( arg0.getOrderNum() );
        if ( arg0.getLeader() != null ) {
            arg1.setLeader( String.valueOf( arg0.getLeader() ) );
        }
        else {
            arg1.setLeader( null );
        }
        arg1.setPhone( arg0.getPhone() );
        arg1.setEmail( arg0.getEmail() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setAncestors( arg0.getAncestors() );
        if ( arg1.getChildren() != null ) {
            List<SysDept> list = arg0.getChildren();
            if ( list != null ) {
                arg1.getChildren().clear();
                arg1.getChildren().addAll( list );
            }
            else {
                arg1.setChildren( null );
            }
        }
        else {
            List<SysDept> list = arg0.getChildren();
            if ( list != null ) {
                arg1.setChildren( new ArrayList<SysDept>( list ) );
            }
        }

        return arg1;
    }
}
