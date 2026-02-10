package com.meta.community_be.likes.articleLike.service;

import com.meta.community_be.article.domain.Article;
import com.meta.community_be.article.service.ArticleService;
import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.likes.articleLike.domain.ArticleLike;
import com.meta.community_be.likes.articleLike.dto.ArticleLikeResponseDto;
import com.meta.community_be.likes.articleLike.repository.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleService articleService;

    @Transactional
    public ArticleLikeResponseDto toggleArticleLike(PrincipalDetails principalDetails, Long boardId, Long articleId) {
        Article article = articleService.getValidBoardAndArticleById(articleId, boardId);

        User logginedUser = principalDetails.getUser();

        Optional<ArticleLike> existingLike = articleLikeRepository.findByArticleAndUser(article, logginedUser);

        boolean liked;
        if (existingLike.isPresent()) {
            articleLikeRepository.delete(existingLike.get());
            liked = false;
        } else {
            ArticleLike newLike = new ArticleLike(article, logginedUser);
            articleLikeRepository.save(newLike);
            liked = true;
        }

        int currentLikesCount = (int) articleLikeRepository.countByArticle(article);

        return new ArticleLikeResponseDto(article.getId(), logginedUser.getId(), liked, currentLikesCount);
    }
}