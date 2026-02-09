package com.meta.community_be.comment.controller;

import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.comment.dto.CommentRequestDto;
import com.meta.community_be.comment.dto.CommentResponseDto;
import com.meta.community_be.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/boards/{boardId}/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable Long boardId,
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        CommentResponseDto commentResponseDto = commentService.createComment(commentRequestDto, boardId, articleId, principalDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(
            @PathVariable Long boardId,
            @PathVariable Long articleId,
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        CommentResponseDto commentResponseDto = commentService.getCommentById(boardId, articleId, id, principalDetails);
        return ResponseEntity.ok(commentResponseDto);
    }
}