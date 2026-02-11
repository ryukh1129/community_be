package com.meta.community_be.likes.commentLike.service;

import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.comment.domain.Comment;
import com.meta.community_be.comment.service.CommentService;
import com.meta.community_be.likes.commentLike.domain.CommentLike;
import com.meta.community_be.likes.commentLike.dto.CommentLikeResponseDto;
import com.meta.community_be.likes.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;

    @Transactional
    public CommentLikeResponseDto toggleCommentLike(PrincipalDetails principalDetails, Long boardId, Long articleId, Long commentId) {
        Comment comment = commentService.getValidComment(boardId, articleId, commentId);

        User logginedUser = principalDetails.getUser();

        Optional<CommentLike> existingLike = commentLikeRepository.findByCommentAndUser(comment, logginedUser);

        boolean liked;
        if (existingLike.isPresent()) {
            commentLikeRepository.delete(existingLike.get());
            liked = false;
        } else {
            CommentLike newLike = new CommentLike(comment, logginedUser);
            commentLikeRepository.save(newLike);
            liked = true;
        }

        int currentLikesCount = (int) commentLikeRepository.countByComment(comment);

        return new CommentLikeResponseDto(comment.getId(), logginedUser.getId(), liked, currentLikesCount);
    }
}