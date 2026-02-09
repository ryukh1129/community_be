package com.meta.community_be.likes.commentLike.controller;

import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.likes.commentLike.dto.CommentLikeResponseDto;
import com.meta.community_be.likes.commentLike.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/boards/{boardId}/articles/{articleId}/comments/{commentId}/likes")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping()
    public ResponseEntity<CommentLikeResponseDto> toggleLikeArticle(
            @PathVariable Long boardId,
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        CommentLikeResponseDto response = commentLikeService.toggleCommentLike(principalDetails, boardId, articleId, commentId);
        return ResponseEntity.ok(response);
    }
}