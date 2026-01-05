package org.dromara.system.domain.vo;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.SysPost;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-05T10:23:18+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class SysPostVoToSysPostMapperImpl implements SysPostVoToSysPostMapper {

    @Override
    public SysPost convert(SysPostVo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysPost sysPost = new SysPost();

        sysPost.setCreateTime( arg0.getCreateTime() );
        sysPost.setPostId( arg0.getPostId() );
        sysPost.setDeptId( arg0.getDeptId() );
        sysPost.setPostCode( arg0.getPostCode() );
        sysPost.setPostName( arg0.getPostName() );
        sysPost.setPostCategory( arg0.getPostCategory() );
        sysPost.setPostSort( arg0.getPostSort() );
        sysPost.setStatus( arg0.getStatus() );
        sysPost.setRemark( arg0.getRemark() );

        return sysPost;
    }

    @Override
    public SysPost convert(SysPostVo arg0, SysPost arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setCreateTime( arg0.getCreateTime() );
        arg1.setPostId( arg0.getPostId() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setPostCode( arg0.getPostCode() );
        arg1.setPostName( arg0.getPostName() );
        arg1.setPostCategory( arg0.getPostCategory() );
        arg1.setPostSort( arg0.getPostSort() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setRemark( arg0.getRemark() );

        return arg1;
    }
}
