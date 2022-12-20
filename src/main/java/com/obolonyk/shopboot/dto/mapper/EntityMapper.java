package com.obolonyk.shopboot.dto.mapper;

import com.obolonyk.shopboot.dto.ProductDto;
import com.obolonyk.shopboot.dto.UserDto;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface EntityMapper {

    User dtoToEntity(UserDto dto);

    Product dtoToEntity(ProductDto dto);
}
