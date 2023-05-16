package com.poriadin.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginMessageDTO {
    String message;
    Boolean status;
}