package com.poriadin.portfolio.repositories;

import com.poriadin.portfolio.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    List<User> findOneByEmailAndPassword(String email, String password);

    User findByEmail(String email);
}