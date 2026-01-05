package org.dromara.system.domain.vo;

import io.github.linpeilie.AutoMapperConfig__100;
import io.github.linpeilie.BaseMapper;
import org.dromara.system.domain.ProStaff;
import org.dromara.system.domain.ProStaffToProStaffVoMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__100.class,
    uses = {ProStaffToProStaffVoMapper.class},
    imports = {}
)
public interface ProStaffVoToProStaffMapper extends BaseMapper<ProStaffVo, ProStaff> {
}
