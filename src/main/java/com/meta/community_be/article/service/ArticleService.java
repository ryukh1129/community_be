package com.meta.community_be.article.service;

import com.meta.community_be.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
}
