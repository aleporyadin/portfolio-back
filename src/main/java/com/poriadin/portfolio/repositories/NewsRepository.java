package com.poriadin.portfolio.repositories;

import com.poriadin.portfolio.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
@EnableJpaRepositories
public interface NewsRepository extends JpaRepository<News, String> {
    Page<News> findAll(Pageable pageable);
}