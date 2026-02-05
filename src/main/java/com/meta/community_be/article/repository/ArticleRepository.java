package com.meta.community_be.article.repository;

import com.meta.community_be.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findAllByOrderByCreatedAtDesc();

    Optional<Article> findByIdAndBoardId(Long id, Long boardId);

    List<Article> findAllByBoardIdOrderByCreatedAtDesc(Long boardId);

    // boardId로 조회하되, 페이징 정보를 적용하여 Page 객체 반환
    Page<Article> findAllByBoardId(Long boardId, Pageable pageable);
}