package com.meta.community_be.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String contents;
    private Long parentCommentId;
}