package com.poriadin.portfolio.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<?> home() {

        return ResponseEntity.ok("HEEELLLO");
    }

}