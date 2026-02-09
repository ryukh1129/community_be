package com.meta.community_be.comment.domain;

import com.meta.community_be.article.domain.Article;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.comment.dto.CommentRequestDto;
import com.meta.community_be.common.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 재귀참조(Recursive) 관계 설정
     * ex) 댓글과 대댓글 관계 / 카테고리의 상위카테고리와 하위카테고리 관계 등에 사용된다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> childComments = new ArrayList<>();

    public Comment(String contents, Article article, User user) {
        this.contents = contents;
        this.parentComment = null;
        this.article = article;
        this.user = user;
    }

    public Comment(String contents, Comment parentComment, Article article, User user) {
        if (parentComment == null) {
            throw new IllegalArgumentException("부모 댓글은 null이 될 수 없습니다.");
        }
        if (!article.getId().equals(parentComment.getArticle().getId())) {
            throw new IllegalArgumentException("대댓글은 부모 댓글과 동일한 게시글에 속해야 합니다.");
        }
        this.contents = contents;
        this.parentComment = parentComment;
        this.article = article;
        this.user = user;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.contents = contents;
    }
}