package com.meta.community_be.article.repository;

import com.meta.community_be.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findAllByOrderByCreatedAtDesc();

    Optional<Article> findByIdAndBoardId(Long id, Long boardId);

    List<Article> findAllByBoardIdOrderByCreatedAtDesc(Long boardId);
}