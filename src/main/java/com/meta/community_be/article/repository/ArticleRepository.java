package com.meta.community_be.article.repository;

import com.meta.community_be.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
