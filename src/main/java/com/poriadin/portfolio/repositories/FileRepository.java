package com.poriadin.portfolio.repositories;

import com.poriadin.portfolio.entities.File;
import com.poriadin.portfolio.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Page<File> findByUser(User user, Pageable pageable);

    Optional<File> findByIdAndUser(Integer fileId, User user);

    Optional<File> findByNameAndUser(String fileName, User user);
}
