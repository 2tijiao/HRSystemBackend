package org.dromara.system.domain.vo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.SysOperLog;
import org.dromara.system.domain.SysOperLogToSysOperLogVoMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {SysOperLogToSysOperLogVoMapper.class},
    imports = {}
)
public interface SysOperLogVoToSysOperLogMapper extends BaseMapper<SysOperLogVo, SysOperLog> {
}
