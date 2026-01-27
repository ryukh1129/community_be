package com.meta.community_be.article.controller;

import com.meta.community_be.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
}
