package com.meta.community_be.comment.service;

import com.meta.community_be.article.domain.Article;
import com.meta.community_be.article.service.ArticleService;
import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.comment.domain.Comment;
import com.meta.community_be.comment.dto.CommentRequestDto;
import com.meta.community_be.comment.dto.CommentResponseDto;
import com.meta.community_be.comment.repository.CommentRepository;
import com.meta.community_be.likes.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long boardId, Long articleId, PrincipalDetails principalDetails) {
        User logginedUser = principalDetails.getUser();
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

    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long boardId, Long articleId, Long commentId, PrincipalDetails principalDetails) {
        Comment comment = getValidComment(boardId, articleId, commentId);

        int likesCount = (int) commentLikeRepository.countByComment(comment);
        boolean liked = false;

        if (principalDetails != null) {
            User logginedUser = principalDetails.getUser();
            liked = commentLikeRepository.findByCommentAndUser(comment, logginedUser).isPresent();
        }

        return new CommentResponseDto(comment, likesCount, liked);
    }

    // id로 부모 댓글과 게시글 찾는 헬퍼 메서드
    public Comment getValidParentComment(Long parentCommentId, Article article) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 부모 댓글은 존재하지 않습니다."));
        if (!parentComment.getArticle().getId().equals(article.getId())) {
            throw new IllegalArgumentException("대댓글은 부모 댓글과 동일한 게시글에 속해야 합니다.");
        }
        return parentComment;
    }

    // id로 게시판, 게시글, 댓글 찾는 헬퍼 메서드
    public Comment getValidComment(Long boardId, Long articleId, Long commentId) {
        articleService.getValidBoardAndArticleById(articleId, boardId);

        return commentRepository.findByIdAndArticleId(commentId, articleId).orElseThrow(() ->
                new IllegalArgumentException("게시글(ID: " + articleId + ")에서 댓글(ID: " + commentId + ")을 찾을 수 없습니다.")
        );
    }
}