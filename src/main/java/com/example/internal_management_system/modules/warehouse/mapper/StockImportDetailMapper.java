package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.StockImportDetailDto;
import com.example.internal_management_system.modules.warehouse.model.StockImportDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockImportDetailMapper {

    @Mapping(target = "importCode", source = "stockImport.importCode")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.productCode")
    StockImportDetailDto toDto(StockImportDetail stockImportDetail);

    @Mapping(target = "stockImport", ignore = true)
    @Mapping(target = "product", ignore = true)
    StockImportDetail toEntity(StockImportDetailDto stockImportDetailDto);
}
