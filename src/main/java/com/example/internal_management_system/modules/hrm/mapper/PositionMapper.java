package com.example.internal_management_system.modules.hrm.mapper;

import com.example.internal_management_system.modules.hrm.dto.PositionDto;
import com.example.internal_management_system.modules.hrm.model.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionDto toDto(Position position);

    Position toEntity(PositionDto positionDto);
}
