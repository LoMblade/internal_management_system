package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.WarehouseDto;
import com.example.internal_management_system.modules.warehouse.model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    WarehouseDto toDto(Warehouse warehouse);

    @Mapping(target = "inventories", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Warehouse toEntity(WarehouseDto warehouseDto);

    @Named("statusToString")
    default String statusToString(Warehouse.WarehouseStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default Warehouse.WarehouseStatus stringToStatus(String status) {
        return status != null ? Warehouse.WarehouseStatus.valueOf(status) : null;
    }
}
