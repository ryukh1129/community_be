package com.meta.community_be.auth.handler;

import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증된 PrincipalDetails 객체 가져오기(우리 시스템의 UserDetails)
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateToken(principalDetails);

        // 클라이언트로 JWT를 전달하는방식은 여러가지가 있다.
        // 1. JWT를 URL쿼리 파라미터로 포함하는 경우
        // 2. JWT를 HTTP 응답 바디에 포함하고 JSON으로 반환하는 경우 (SPA에서 주로 사용)

        // 현재 구현에서는 1번을 활용 할 것임 (가장 일반적인 OAuth2 리다이렉트 연동 방식)
        // 3. 리다이렉트 URL 생성 (UriComponentsBuilder 사용 추천)
        // 결과 예시: http://localhost:5173/login/google/callback/success?accessToken=eyJh...
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        response.sendRedirect(targetUrl);
    }
}