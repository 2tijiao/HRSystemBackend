package org.dromara.system.domain.vo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.ProStaffEmnumber;
import org.dromara.system.domain.ProStaffEmnumberToProStaffEmnumberVoMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {ProStaffEmnumberToProStaffEmnumberVoMapper.class},
    imports = {}
)
public interface ProStaffEmnumberVoToProStaffEmnumberMapper extends BaseMapper<ProStaffEmnumberVo, ProStaffEmnumber> {
}
