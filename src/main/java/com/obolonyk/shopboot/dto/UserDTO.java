package com.obolonyk.shopboot.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private String lastName;
    private String login;
    private String email;
    private String password;
}
