package com.meta.community_be.auth.service;

import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.auth.domain.UserRole;
import com.meta.community_be.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 제공자 부분 Google
        String oAuth2Id = oAuth2User.getName(); // Google의 sub 필드값

        // OAuth2User의 attributes에서 이메일과 닉네임 등 정보 추출하는 부분
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email"); // 이메일 정보 가져오기
        String nickname = (String) attributes.get("name");

        // 우리 백엔드 프로젝트 시스템에 맞는 username으로 변환 (소셜)
        String username = registrationId + ":" + oAuth2Id; // 예: google_1234567890

        // [[DB에 사용자 정보가 있는지 확인, 없는 경우엔 회원가입 처리 -> 소셜로그인 인증객체 반환]]
        User oAuth2user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User(
                            username,
                            nickname,
                            passwordEncoder.encode(UUID.randomUUID().toString()), // 임시 비밀번호
                            email,
                            UserRole.ROLE_USER // 기본 권한 설정
                    );
                    return userRepository.save(newUser);
                });

        return new PrincipalDetails(oAuth2user, attributes);
    }
}