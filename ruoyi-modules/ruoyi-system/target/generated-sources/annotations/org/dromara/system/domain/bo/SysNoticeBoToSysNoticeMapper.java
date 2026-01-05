package org.dromara.system.domain.bo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.SysNotice;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {},
    imports = {}
)
public interface SysNoticeBoToSysNoticeMapper extends BaseMapper<SysNoticeBo, SysNotice> {
}
