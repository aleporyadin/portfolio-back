package com.poriadin.portfolio.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poriadin.portfolio.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    private String avatarPath;

    @JsonIgnore
    private String password;

    private String firstName;

    private String lastName;

    private Date birthdate;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password, String avatarPath,
                           Collection<? extends GrantedAuthority> authorities,
                           String firstName, String lastName, Date birthdate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatarPath = avatarPath;
        this.authorities = authorities;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;

    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAvatarPath(),
                authorities,
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}