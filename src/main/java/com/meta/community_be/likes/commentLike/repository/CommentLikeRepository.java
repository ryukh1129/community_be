package com.meta.community_be.likes.commentLike.repository;

import com.meta.community_be.auth.domain.User;
import com.meta.community_be.comment.domain.Comment;
import com.meta.community_be.likes.commentLike.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    long countByComment(Comment comment);
}