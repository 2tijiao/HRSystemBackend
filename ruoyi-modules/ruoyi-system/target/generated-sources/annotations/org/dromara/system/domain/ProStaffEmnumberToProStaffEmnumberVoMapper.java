package org.dromara.system.domain;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.bo.ProStaffEmnumberBoToProStaffEmnumberMapper;
import org.dromara.system.domain.vo.ProStaffEmnumberVo;
import org.dromara.system.domain.vo.ProStaffEmnumberVoToProStaffEmnumberMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {ProStaffEmnumberVoToProStaffEmnumberMapper.class,ProStaffEmnumberBoToProStaffEmnumberMapper.class},
    imports = {}
)
public interface ProStaffEmnumberToProStaffEmnumberVoMapper extends BaseMapper<ProStaffEmnumber, ProStaffEmnumberVo> {
}
