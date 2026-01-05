package org.dromara.system.domain.vo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.SysDictData;
import org.dromara.system.domain.SysDictDataToSysDictDataVoMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {SysDictDataToSysDictDataVoMapper.class},
    imports = {}
)
public interface SysDictDataVoToSysDictDataMapper extends BaseMapper<SysDictDataVo, SysDictData> {
}
