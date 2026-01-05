package org.dromara.system.domain.vo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.SysTenant;
import org.dromara.system.domain.SysTenantToSysTenantVoMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {SysTenantToSysTenantVoMapper.class},
    imports = {}
)
public interface SysTenantVoToSysTenantMapper extends BaseMapper<SysTenantVo, SysTenant> {
}
