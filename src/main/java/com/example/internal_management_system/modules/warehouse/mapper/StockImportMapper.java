package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.StockImportDto;
import com.example.internal_management_system.modules.warehouse.model.StockImport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StockImportMapper {

    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    StockImportDto toDto(StockImport stockImport);

    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "importDetails", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    StockImport toEntity(StockImportDto stockImportDto);

    @Named("statusToString")
    default String statusToString(StockImport.ImportStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default StockImport.ImportStatus stringToStatus(String status) {
        return status != null ? StockImport.ImportStatus.valueOf(status) : null;
    }
}
