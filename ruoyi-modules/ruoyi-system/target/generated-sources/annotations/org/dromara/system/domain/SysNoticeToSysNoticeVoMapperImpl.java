package org.dromara.system.domain;

import javax.annotation.processing.Generated;
import org.dromara.system.domain.vo.SysNoticeVo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-08T12:17:39+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (BellSoft)"
)
@Component
public class SysNoticeToSysNoticeVoMapperImpl implements SysNoticeToSysNoticeVoMapper {

    @Override
    public SysNoticeVo convert(SysNotice arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysNoticeVo sysNoticeVo = new SysNoticeVo();

        sysNoticeVo.setNoticeId( arg0.getNoticeId() );
        sysNoticeVo.setNoticeTitle( arg0.getNoticeTitle() );
        sysNoticeVo.setNoticeType( arg0.getNoticeType() );
        sysNoticeVo.setNoticeContent( arg0.getNoticeContent() );
        sysNoticeVo.setStatus( arg0.getStatus() );
        sysNoticeVo.setRemark( arg0.getRemark() );
        if ( arg0.getCreateBy() != null ) {
            sysNoticeVo.setCreateBy( Long.parseLong( arg0.getCreateBy() ) );
        }
        sysNoticeVo.setCreateTime( arg0.getCreateTime() );

        return sysNoticeVo;
    }

    @Override
    public SysNoticeVo convert(SysNotice arg0, SysNoticeVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setNoticeId( arg0.getNoticeId() );
        arg1.setNoticeTitle( arg0.getNoticeTitle() );
        arg1.setNoticeType( arg0.getNoticeType() );
        arg1.setNoticeContent( arg0.getNoticeContent() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setRemark( arg0.getRemark() );
        if ( arg0.getCreateBy() != null ) {
            arg1.setCreateBy( Long.parseLong( arg0.getCreateBy() ) );
        }
        else {
            arg1.setCreateBy( null );
        }
        arg1.setCreateTime( arg0.getCreateTime() );

        return arg1;
    }
}
