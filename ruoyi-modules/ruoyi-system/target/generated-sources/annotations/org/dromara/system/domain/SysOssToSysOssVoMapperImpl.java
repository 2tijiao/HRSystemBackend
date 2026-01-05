package org.dromara.system.domain;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.vo.SysOssVo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-05T10:23:18+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class SysOssToSysOssVoMapperImpl implements SysOssToSysOssVoMapper {

    @Override
    public SysOssVo convert(SysOss arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysOssVo sysOssVo = new SysOssVo();

        sysOssVo.setOssId( arg0.getOssId() );
        sysOssVo.setFileName( arg0.getFileName() );
        sysOssVo.setOriginalName( arg0.getOriginalName() );
        sysOssVo.setFileSuffix( arg0.getFileSuffix() );
        sysOssVo.setUrl( arg0.getUrl() );
        sysOssVo.setExt1( arg0.getExt1() );
        sysOssVo.setCreateTime( arg0.getCreateTime() );
        if ( arg0.getCreateBy() != null ) {
            sysOssVo.setCreateBy( Long.parseLong( arg0.getCreateBy() ) );
        }
        sysOssVo.setService( arg0.getService() );

        return sysOssVo;
    }

    @Override
    public SysOssVo convert(SysOss arg0, SysOssVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setOssId( arg0.getOssId() );
        arg1.setFileName( arg0.getFileName() );
        arg1.setOriginalName( arg0.getOriginalName() );
        arg1.setFileSuffix( arg0.getFileSuffix() );
        arg1.setUrl( arg0.getUrl() );
        arg1.setExt1( arg0.getExt1() );
        arg1.setCreateTime( arg0.getCreateTime() );
        if ( arg0.getCreateBy() != null ) {
            arg1.setCreateBy( Long.parseLong( arg0.getCreateBy() ) );
        }
        else {
            arg1.setCreateBy( null );
        }
        arg1.setService( arg0.getService() );

        return arg1;
    }
}
