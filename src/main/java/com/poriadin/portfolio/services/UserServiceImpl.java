package com.poriadin.portfolio.services;

import com.poriadin.portfolio.dto.LoginDTO;
import com.poriadin.portfolio.dto.LoginMessageDTO;
import com.poriadin.portfolio.dto.UserDTO;
import com.poriadin.portfolio.entities.User;
import com.poriadin.portfolio.repositories.UserRepo;
import com.poriadin.portfolio.services.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo employeeRepo;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo employeeRepo, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String addUser(UserDTO userDTO) {

        User employee = new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail(),
                this.passwordEncoder.encode(userDTO.getPassword())
        );

        employeeRepo.save(employee);

        return employee.getName();
    }

    @Override
    public LoginMessageDTO loginUser(LoginDTO loginDTO) {
        String msg = "";
        User user = employeeRepo.findByEmail(loginDTO.getEmail());
        if (user != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                List<User> users = employeeRepo.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (!users.isEmpty()) {
                    return new LoginMessageDTO("Login Success", true);
                } else {
                    return new LoginMessageDTO("Login Failed", false);
                }
            } else {

                return new LoginMessageDTO("password Not Match", false);
            }
        } else {
            return new LoginMessageDTO("Email not exits", false);
        }
    }

}