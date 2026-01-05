package org.dromara.system.domain;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.bo.ProStaffBoToProStaffMapper;
import org.dromara.system.domain.vo.ProStaffVo;
import org.dromara.system.domain.vo.ProStaffVoToProStaffMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {ProStaffBoToProStaffMapper.class,ProStaffVoToProStaffMapper.class},
    imports = {}
)
public interface ProStaffToProStaffVoMapper extends BaseMapper<ProStaff, ProStaffVo> {
}
