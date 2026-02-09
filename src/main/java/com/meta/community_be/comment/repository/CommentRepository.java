package com.meta.community_be.comment.repository;

import com.meta.community_be.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId);

    Optional<Comment> findByIdAndArticleId(Long commentId, Long articleId);
}