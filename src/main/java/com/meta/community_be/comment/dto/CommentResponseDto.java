package com.meta.community_be.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.community_be.comment.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private Long articleId;
    private Long parentCommentId;
    private String authorUsername;
    private String authorNickname;
    private boolean liked;
    private int likesCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment, int likesCount, boolean liked) {
        this.id = comment.getId();
        this.contents = comment.getContents();

        if (comment.getUser() != null) {
            this.authorUsername = comment.getUser().getUsername();
            this.authorNickname = comment.getUser().getNickname();
        }

        if (comment.getArticle() != null) {
            this.articleId = comment.getArticle().getId();
        } else {
            this.articleId = null;
        }
        this.parentCommentId = (comment.getParentComment() != null) ? comment.getParentComment().getId() : null;
        this.likesCount = likesCount;
        this.liked = liked;
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();

        if (comment.getArticle() != null) {
            this.articleId = comment.getArticle().getId();
        } else {
            this.articleId = null;
        }

        this.parentCommentId = (comment.getParentComment() != null) ? comment.getParentComment().getId() : null;

        this.likesCount = 0;
        this.liked = false;

        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}