package com.obolonyk.shopboot.util;

import com.obolonyk.shopboot.dto.ProductDTO;
import com.obolonyk.shopboot.dto.UserDTO;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.entity.User;

public class PojoDtoConverter {

    public static UserDTO getUserDTOFromUserEntity(User user){
        return UserDTO
                .builder()
                .login(user.getLogin())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .id(user.getId())
                .build();
    }

    public static ProductDTO getProductDTOFromProductEntity(Product product){
        return ProductDTO
                .builder()
                .creationDate(product.getCreationDate())
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public static User getUserFromUserDTO(UserDTO userDTO){
        return User
                .builder()
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .lastName(userDTO.getLastName())
                .password(userDTO.getPassword())
                .build();
    }

    public static Product getProductFromProductDTO(ProductDTO productDTO){
        return Product
                .builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();
    }

}
