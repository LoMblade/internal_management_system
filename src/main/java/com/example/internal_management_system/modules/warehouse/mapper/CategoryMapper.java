package com.example.internal_management_system.modules.warehouse.mapper;

import com.example.internal_management_system.modules.warehouse.dto.CategoryDto;
import com.example.internal_management_system.modules.warehouse.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryName", source = "parentCategory.name")
    CategoryDto toDto(Category category);

    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryDto categoryDto);
}
