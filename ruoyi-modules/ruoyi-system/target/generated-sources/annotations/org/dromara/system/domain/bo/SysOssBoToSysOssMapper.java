package org.dromara.system.domain.bo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.SysOss;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {},
    imports = {}
)
public interface SysOssBoToSysOssMapper extends BaseMapper<SysOssBo, SysOss> {
}
