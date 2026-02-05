package com.meta.community_be.auth.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Record 클래스는 Java 14에서 도입된 기능으로, 불변(immutable) 데이터 객체를 간편하게 정의
 * 아래와 같은 특징을 가지고 있다.
 * 생성자는 모든 field를 포함합니다
 * toString()도 모든 field를 포함합니다.
 * equals(), hashCode() 메서드는 invokedynamic based mechanism을 사용합니다.
 * getter는 field이름과 유사한 이름으로 생성됩니다 ex) id(), email()..
 * 기본적으로 java.lang.Record class를 상속받기 때문에 다른 class를 상속받을 수 없습니다.
 * class가 final이기 때문에 다른 subclass를 생성할 수 없습니다.
 * 모든 field는 불변이기 때문에 setter는 제공하지 않습니다.
 */
public record PrincipalDetails(User user) implements UserDetails {
    //    private Map<String, Object> attributes; // OAuth2 attributes (필요시 사용)

    // 1. 일반 로그인(UserDetailsServiceImpl)을 위한 생성자
    //        this.attributes = null;

    // 2. OAuth2 로그인(CustomOAuth2UserService)을 위한 생성자 (필요시 사용)
//    public PrincipalDetails(User user, Map<String, Object> attributes) {
//        this.user = user;
//        this.attributes = attributes;
//    }

    // -- UserDetails 인터페이스에 존재하는 추상메서드 구현체 부분 (Override) --
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.user.getUserRole());
    }

    // 계정 만료 여부(true = 만료되지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부(true = 잠금되지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료(자격증명) 여부(true = 만료되지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부(true = 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

//     --- OAuth2User 인터페이스 메서드 구현 (필요시 사용)
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes != null ? attributes : Collections.emptyMap();
//    }
//
//    @Override
//    public String getName() {
//        return this.user.getUsername();
//    }
}