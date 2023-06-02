package com.poriadin.portfolio.repositories;

import com.poriadin.portfolio.entities.File;
import com.poriadin.portfolio.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByUser(User user);

    Optional<File> findByIdAndUser(Integer fileId, User user);
}
