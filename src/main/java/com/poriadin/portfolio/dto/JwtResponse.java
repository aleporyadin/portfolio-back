package com.poriadin.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String token;
    private String type;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private List<String> roles;
    private byte[] avatar;
    private Date birthdate;

}