package com.poriadin.portfolio.services.interfaces;

import com.poriadin.portfolio.dto.LoginDTO;
import com.poriadin.portfolio.dto.LoginMessageDTO;
import com.poriadin.portfolio.dto.UserDTO;

public interface UserService {
    String addUser(UserDTO employeeDTO);

    LoginMessageDTO loginUser(LoginDTO loginDTO);

}