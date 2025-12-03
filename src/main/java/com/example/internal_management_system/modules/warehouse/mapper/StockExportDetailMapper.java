package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDetailDto;
import com.example.internal_management_system.modules.warehouse.model.StockExportDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockExportDetailMapper {

    @Mapping(target = "exportCode", source = "stockExport.exportCode")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.productCode")
    StockExportDetailDto toDto(StockExportDetail stockExportDetail);

    @Mapping(target = "stockExport", ignore = true)
    @Mapping(target = "product", ignore = true)
    StockExportDetail toEntity(StockExportDetailDto stockExportDetailDto);
}
