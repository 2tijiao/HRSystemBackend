package org.dromara.system.domain.bo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.common.log.event.OperLogEvent;
import org.dromara.common.log.event.OperLogEventToSysOperLogBoMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {SysOperLogBoToSysOperLogMapper.class,OperLogEventToSysOperLogBoMapper.class},
    imports = {}
)
public interface SysOperLogBoToOperLogEventMapper extends BaseMapper<SysOperLogBo, OperLogEvent> {
}
