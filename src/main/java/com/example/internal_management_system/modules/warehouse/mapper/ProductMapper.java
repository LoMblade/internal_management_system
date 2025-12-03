package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.ProductDto;
import com.example.internal_management_system.modules.warehouse.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    ProductDto toDto(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Product toEntity(ProductDto productDto);

    @Named("statusToString")
    default String statusToString(Product.ProductStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default Product.ProductStatus stringToStatus(String status) {
        return status != null ? Product.ProductStatus.valueOf(status) : null;
    }
}
