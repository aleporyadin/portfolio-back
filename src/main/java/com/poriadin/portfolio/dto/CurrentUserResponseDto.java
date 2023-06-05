package com.poriadin.portfolio.dto;

import com.poriadin.portfolio.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponseDto {
    private Long id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private Set<Role> roles;
    private String avatarPath;
    private Date birthdate;
}
