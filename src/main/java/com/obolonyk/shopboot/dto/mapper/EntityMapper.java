package com.obolonyk.shopboot.dto.mapper;

import com.obolonyk.shopboot.dto.ProductDTO;
import com.obolonyk.shopboot.dto.UserDTO;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface EntityMapper {

    User dtoToEntity(UserDTO dto);

    Product dtoToEntity(ProductDTO dto);
}
