package com.obolonyk.shopboot.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    private Integer id;

    @Column
    private String name;
    @Column
    private String lastName;
    @Column
    private String login;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String salt;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
}
