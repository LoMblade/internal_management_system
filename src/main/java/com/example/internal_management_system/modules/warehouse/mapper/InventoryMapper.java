package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.InventoryDto;
import com.example.internal_management_system.modules.warehouse.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.productCode")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    InventoryDto toDto(Inventory inventory);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    Inventory toEntity(InventoryDto inventoryDto);
}
