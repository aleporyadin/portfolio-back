package com.poriadin.portfolio.services;

import com.poriadin.portfolio.entities.News;
import com.poriadin.portfolio.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }


    public List<News> getNewsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return (List<News>) newsRepository.findAll(pageable);
    }

    public Optional<News> getNewsByTitle(String title) {
        return newsRepository.findById(title);
    }

}
