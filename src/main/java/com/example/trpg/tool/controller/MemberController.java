package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.common.ApiResponse;
import com.example.trpg.tool.dto.member.LoginRequestDTO;
import com.example.trpg.tool.dto.member.MemberRegisterRequestDTO;
import com.example.trpg.tool.dto.member.MemberUpdateRequest;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.security.CustomUserDetails;
import com.example.trpg.tool.security.dto.TokenResponse;
import com.example.trpg.tool.security.dto.MemberInfoDTO;
import com.example.trpg.tool.security.dto.PasswordUpdateRequest;
import com.example.trpg.tool.security.util.JwtUtil;
import com.example.trpg.tool.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Log4j2
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /** ✅ 회원가입 */
    @PostMapping("/join")
    public ResponseEntity<String> register(@RequestBody MemberRegisterRequestDTO dto) {
        dto.setUserRole("USER"); // 프론트에서 뭐 보내든 강제로 USER
        memberService.register(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    /** ✅ 로그인 → 토큰 발급 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // ✅ 권한 리스트 추출
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String accessToken = jwtUtil.createAccessToken(userDetails.getUsername(), roles);
            String refreshToken = jwtUtil.createRefreshToken(userDetails.getUsername());

            TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
            return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", tokenResponse));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "로그인 실패: 아이디 또는 비밀번호 오류", null));
        }
    }

    /** ✅ 현재 사용자 정보 조회 */
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("member me");
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보 없음");
        }
        return ResponseEntity.ok(new MemberInfoDTO(userDetails.getMember()));
    }

    /** ✅ 로그아웃 (세션 무효화, 쿠키 제거) */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        jwtUtil.clearRefreshTokenCookie(response);
        return ResponseEntity.ok("로그아웃 성공");
    }

    /** ✅ 사용자 정보 수정 */
    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody MemberUpdateRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Member loginMember = userDetails.getMember();
        memberService.updateMember(loginMember.getMid(), dto);
        return ResponseEntity.ok().build();
    }

    /** ✅ 비밀번호 변경 */
    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody PasswordUpdateRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        memberService.updatePassword(userDetails.getMember().getMid(), dto);
        return ResponseEntity.ok().build();
    }

    /** ✅ Refresh Token으로 access 재발급 */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.resolveRefreshTokenFromCookie(request);

        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.getUsername(refreshToken);

            // 🔧 여기서 DB 조회
            Member member = memberService.findByUserId(username); // 또는 repository 직접 사용
            List<String> roles = List.of("ROLE_" + member.getUserRole());

            String newAccessToken = jwtUtil.createAccessToken(username, roles);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 만료");
        }
    }
}
