package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDto;
import com.example.internal_management_system.modules.warehouse.model.StockExport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StockExportMapper {

    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "type", source = "type", qualifiedByName = "typeToString")
    StockExportDto toDto(StockExport stockExport);

    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "exportDetails", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToType")
    StockExport toEntity(StockExportDto stockExportDto);

    @Named("statusToString")
    default String statusToString(StockExport.ExportStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default StockExport.ExportStatus stringToStatus(String status) {
        return status != null ? StockExport.ExportStatus.valueOf(status) : null;
    }

    @Named("typeToString")
    default String typeToString(StockExport.ExportType type) {
        return type != null ? type.name() : null;
    }

    @Named("stringToType")
    default StockExport.ExportType stringToType(String type) {
        return type != null ? StockExport.ExportType.valueOf(type) : null;
    }
}
