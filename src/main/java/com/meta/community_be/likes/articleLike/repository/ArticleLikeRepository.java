package com.meta.community_be.likes.articleLike.repository;

import com.meta.community_be.article.domain.Article;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.likes.articleLike.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByArticleAndUser(Article article, User user);

    long countByArticle(Article article);
}