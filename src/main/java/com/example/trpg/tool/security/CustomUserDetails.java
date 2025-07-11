package com.example.trpg.tool.security;

import com.example.trpg.tool.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정: 예시로 "ROLE_USER" 반환
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getUserRole()));
    }

    @Override
    public String getPassword() {
        return member.getPassword(); // 암호화된 비밀번호
    }

    @Override
    public String getUsername() {
        return member.getUserId(); // 로그인할 때 사용할 아이디
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}