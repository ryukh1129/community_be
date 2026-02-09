package com.meta.community_be.article.service;

import com.meta.community_be.article.domain.Article;
import com.meta.community_be.article.dto.ArticleRequestDto;
import com.meta.community_be.article.dto.ArticleResponseDto;
import com.meta.community_be.article.repository.ArticleRepository;
import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.board.domain.Board;
import com.meta.community_be.board.repository.BoardRepository;
import com.meta.community_be.comment.domain.Comment;
import com.meta.community_be.comment.dto.CommentResponseDto;
import com.meta.community_be.file.dto.FileResponseDto;
import com.meta.community_be.file.service.FileService;
import com.meta.community_be.likes.articleLike.repository.ArticleLikeRepository;
import com.meta.community_be.likes.commentLike.domain.CommentLike;
import com.meta.community_be.likes.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final FileService fileService;

    @Transactional
    public ArticleResponseDto createArticle(ArticleRequestDto articleRequestDto, Long boardId, PrincipalDetails principalDetails, MultipartFile file) {
        User logginedUser = principalDetails.user();
        // 해당 id의 게시판이 존재하는지 확인
        Board foundBoard = getValidBoardById(boardId);
        // RequestDto -> Entity 변환
        Article newArticle = new Article(articleRequestDto, foundBoard, logginedUser);
        Article savedArticle = articleRepository.save(newArticle);

        if (file != null && !file.isEmpty()) {
            fileService.uploadFile(savedArticle, file);
        }

        // Entity -> ResponseDto 변환
        ArticleResponseDto articleResponseDto = new ArticleResponseDto(savedArticle);
        return articleResponseDto;
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponseDto> getArticles(Long boardId, Pageable pageable) {
        Page<ArticleResponseDto> articleResponseDtoPaginationList = articleRepository.findAllByBoardId(boardId, pageable)
                .map(ArticleResponseDto::new);
        return articleResponseDtoPaginationList;
    }

    @Transactional(readOnly = true)
    public ArticleResponseDto getArticleById(Long id, Long boardId, PrincipalDetails principalDetails) {
        Article foundArticle = getValidBoardAndArticleById(id, boardId);
        User logginedUser = principalDetails.user();

        int currentLikesCount = (int) articleLikeRepository.countByArticle(foundArticle);
        boolean liked = false;
        if (logginedUser != null) {
            liked = articleLikeRepository.findByArticleAndUser(foundArticle, logginedUser).isPresent();
        }

        List<Comment> comments = foundArticle.getComments();
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        if (!comments.isEmpty()) {
            // 1. 이 게시글의 댓글들에 달린 '모든' 좋아요 데이터를 가져오기 (쿼리 1방)
            List<CommentLike> commentLikes = commentLikeRepository.findAllByCommentIn(comments);

            // 2. 좋아요 개수 계산 (Grouping By Comment ID) -> Map<댓글ID, 개수>
            Map<Long, Integer> likeCountMap = commentLikes.stream()
                    .collect(Collectors.groupingBy(
                            like -> like.getComment().getId(),
                            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                    ));

            // 3. "내가" 좋아요 누른 댓글 ID 목록 추출 -> Set<댓글ID>
            Set<Long> myLikedCommentIds = new HashSet<>();
            if (logginedUser != null) {
                myLikedCommentIds = commentLikes.stream()
                        .filter(like -> like.getUser().getId().equals(logginedUser.getId()))
                        .map(like -> like.getComment().getId())
                        .collect(Collectors.toSet());
            }

            for (Comment comment : comments) {
                int likesCount = likeCountMap.getOrDefault(comment.getId(), 0);
                boolean isLiked = myLikedCommentIds.contains(comment.getId());

                // CommentResponseDto 생성자
                commentResponseDtos.add(new CommentResponseDto(comment, likesCount, isLiked));
            }
        }

        // File Response DTO 생성
        List<FileResponseDto> fileResponseDtos = foundArticle.getFiles().stream()
                .map(file -> new FileResponseDto(
                        file.getOriginalFileName(),
                        file.getStoredFileName(),
                        file.getFilePath()
                )).toList();

        ArticleResponseDto articleResponseDto = new ArticleResponseDto(foundArticle, currentLikesCount, liked, commentResponseDtos, fileResponseDtos);
        return articleResponseDto;
    }

    @Transactional
    public ArticleResponseDto updateArticle(Long id, ArticleRequestDto articleRequestDto, Long boardId) {
        // 해당 id의 게시글이 존재하는지 확인
        Article foundArticle = getValidBoardAndArticleById(id, boardId);
        // 게시글 내용 수정
        foundArticle.update(articleRequestDto);
        return new ArticleResponseDto(foundArticle);
    }

    @Transactional
    public void deleteArticle(Long id, Long boardId) {
        // 해당 id의 게시판과 게시글이 존재하는지 확인
        Article foundArticle = getValidBoardAndArticleById(id, boardId);
        // 게시글 내용 삭제
        articleRepository.delete(foundArticle);
    }

    // id로 게시판과 게시글 찾는 헬퍼 메서드
    public Article getValidBoardAndArticleById(Long articleId, Long boardId) {
        return articleRepository.findByIdAndBoardId(articleId, boardId).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 게시글과 boardId의 게시판은 존재하지 않습니다."));
    }

    public Board getValidBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 게시판은 존재하지 않습니다."));
    }
}