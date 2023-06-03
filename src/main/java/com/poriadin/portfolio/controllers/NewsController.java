package com.poriadin.portfolio.controllers;

import com.poriadin.portfolio.entities.News;
import com.poriadin.portfolio.services.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<Page<News>> getAllNews(@RequestParam int page, @RequestParam int size) {
        Page<News> newsPage = newsService.getNewsByPage(page, size);
        return new ResponseEntity<>(newsPage, HttpStatus.OK);
    }
}
