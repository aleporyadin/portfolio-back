package com.poriadin.portfolio.services;

import com.poriadin.portfolio.entities.News;
import com.poriadin.portfolio.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<News> getAllNews() {
        return (List<News>) newsRepository.findAll();
    }

    public Optional<News> getNewsByTitle(String title) {
        return newsRepository.findById(title);
    }

}
