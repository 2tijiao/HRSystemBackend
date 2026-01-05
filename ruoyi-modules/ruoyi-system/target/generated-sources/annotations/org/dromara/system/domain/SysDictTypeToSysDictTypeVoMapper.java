package org.dromara.system.domain;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.bo.SysDictTypeBoToSysDictTypeMapper;
import org.dromara.system.domain.vo.SysDictTypeVo;
import org.dromara.system.domain.vo.SysDictTypeVoToSysDictTypeMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {SysDictTypeBoToSysDictTypeMapper.class,SysDictTypeVoToSysDictTypeMapper.class},
    imports = {}
)
public interface SysDictTypeToSysDictTypeVoMapper extends BaseMapper<SysDictType, SysDictTypeVo> {
}
