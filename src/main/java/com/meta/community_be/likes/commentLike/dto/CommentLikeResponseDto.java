package com.meta.community_be.likes.commentLike.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentLikeResponseDto {
    private Long commentId;
    private Long userId;
    private boolean liked;
    private int likesCount;

    public CommentLikeResponseDto(Long commentId, Long userId, boolean liked, int likesCount) {
        this.commentId = commentId;
        this.userId = userId;
        this.liked = liked;
        this.likesCount = likesCount;
    }
}