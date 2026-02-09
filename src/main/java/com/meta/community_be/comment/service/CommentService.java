package com.meta.community_be.comment.service;

import com.meta.community_be.article.domain.Article;
import com.meta.community_be.article.repository.ArticleRepository;
import com.meta.community_be.article.service.ArticleService;
import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.comment.domain.Comment;
import com.meta.community_be.comment.dto.CommentRequestDto;
import com.meta.community_be.comment.dto.CommentResponseDto;
import com.meta.community_be.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long boardId, Long articleId, PrincipalDetails principalDetails) {
        User logginedUser = principalDetails.user();
        // 해당 id의 게시글이 존재하는지 확인
        Article foundArticle = articleService.getValidBoardAndArticleById(boardId, articleId);
        // RequestDto -> Entity 변환
        Comment newComment;
        if (commentRequestDto.getParentCommentId() != null) {
            Comment parentComment = getValidParentComment(commentRequestDto.getParentCommentId(), foundArticle);
            newComment = new Comment(commentRequestDto.getContents(), parentComment, foundArticle, logginedUser);
        } else {
            newComment = new Comment(commentRequestDto.getContents(), foundArticle, logginedUser);
        }
        Comment savedComment = commentRepository.save(newComment);
        // Entity -> ResponseDto 변환
        CommentResponseDto commentResponseDto = new CommentResponseDto(savedComment);
        return commentResponseDto;
    }

    // id로 부모 댓글과 \게시글 찾는 헬퍼 메서드
    public Comment getValidParentComment(Long parentCommentId, Article article) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 부모 댓글은 존재하지 않습니다."));
        if (!parentComment.getArticle().getId().equals(article.getId())) {
            throw new IllegalArgumentException("대댓글은 부모 댓글과 동일한 게시글에 속해야 합니다.");
        }
        return parentComment;
    }
}