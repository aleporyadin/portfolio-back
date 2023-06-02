package com.poriadin.portfolio.repositories;

import com.poriadin.portfolio.entities.ZipFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZipFileRepository extends JpaRepository<ZipFile, Long> {
    // Add any additional methods for querying and managing zip files
}
