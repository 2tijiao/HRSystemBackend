package org.dromara.system.domain.vo;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.SysOss;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:37+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class SysOssVoToSysOssMapperImpl implements SysOssVoToSysOssMapper {

    @Override
    public SysOss convert(SysOssVo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysOss sysOss = new SysOss();

        if ( arg0.getCreateBy() != null ) {
            sysOss.setCreateBy( String.valueOf( arg0.getCreateBy() ) );
        }
        sysOss.setCreateTime( arg0.getCreateTime() );
        sysOss.setOssId( arg0.getOssId() );
        sysOss.setFileName( arg0.getFileName() );
        sysOss.setOriginalName( arg0.getOriginalName() );
        sysOss.setFileSuffix( arg0.getFileSuffix() );
        sysOss.setUrl( arg0.getUrl() );
        sysOss.setExt1( arg0.getExt1() );
        sysOss.setService( arg0.getService() );

        return sysOss;
    }

    @Override
    public SysOss convert(SysOssVo arg0, SysOss arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        if ( arg0.getCreateBy() != null ) {
            arg1.setCreateBy( String.valueOf( arg0.getCreateBy() ) );
        }
        else {
            arg1.setCreateBy( null );
        }
        arg1.setCreateTime( arg0.getCreateTime() );
        arg1.setOssId( arg0.getOssId() );
        arg1.setFileName( arg0.getFileName() );
        arg1.setOriginalName( arg0.getOriginalName() );
        arg1.setFileSuffix( arg0.getFileSuffix() );
        arg1.setUrl( arg0.getUrl() );
        arg1.setExt1( arg0.getExt1() );
        arg1.setService( arg0.getService() );

        return arg1;
    }
}
