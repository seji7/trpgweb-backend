package com.example.trpg.tool.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@Log4j2
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expire}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token-expire}")
    private long refreshTokenExpireTime;

    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** ✅ Access Token 생성 */
    public String createAccessToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        log.info("역할 : " + roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpireTime);

        log.info("accessToken make");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** ✅ Refresh Token 생성 */
    public String createRefreshToken(String subject) {
        return createToken(subject, refreshTokenExpireTime);
    }

    /** ✅ JWT 생성 핵심 메서드 */
    private String createToken(String subject, long expireMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireMillis);

        log.info("createToken");

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** ✅ 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        log.info("check Token(valid)");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** ✅ 토큰에서 username 추출 */
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /** ✅ 쿠키에서 Refresh Token 추출 */
    public String resolveRefreshTokenFromCookie(HttpServletRequest request) {
//        log.info("Refresh token making start");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        log.info("Refresh token made");
        return null;
    }

    /** ✅ Refresh Token HttpOnly 쿠키에 저장 (선택사항) */
    public void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // 자바스크립트 접근 불가
        cookie.setPath("/");
        cookie.setMaxAge((int) (refreshTokenExpireTime / 1000)); // 초 단위
        response.addCookie(cookie);
    }

    /** ✅ Refresh Token 쿠키 제거 (로그아웃 시) */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
        log.info("Refresh Token Deleted");
    }
}