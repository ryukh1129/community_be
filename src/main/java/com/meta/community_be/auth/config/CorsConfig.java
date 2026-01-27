package com.meta.community_be.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    // CORS란, Cross-Origin Resource Sharing으로
    // 서로 다른 출처(도메인, 스킴, 포트) 간에 리소스를 공유할 수 있도록
    // 서버가 브라우저에 허용해주는 HTTP 헤더 기반의 메커니즘
    // SOP(Same-Origin Policy, 동일 출처 정책)라는 브라우저의 보안 정책으로 다른 출처 요청이 거부됨
    // 우리가 개발할 React 등 프론트엔드와 같은 다른 포트의 시스템이 외부로 간주되므로 접근할 수 있도록 허용해야 한다.

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173", "http://127.0.0.1:5173", // React + vite FE
                "http://localhost:8000", "http://127.0.0.1:8000", // FastAPI AI BE
                "null"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
