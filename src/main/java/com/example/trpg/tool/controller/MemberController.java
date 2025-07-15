package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.LoginRequestDTO;
import com.example.trpg.tool.dto.MemberRegisterRequestDTO;
import com.example.trpg.tool.dto.MemberUpdateRequest;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.security.CustomUserDetails;
import com.example.trpg.tool.security.dto.MemberInfoDTO;
import com.example.trpg.tool.security.dto.PasswordUpdateRequest;
import com.example.trpg.tool.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/join")
    public ResponseEntity<String> register(@RequestBody MemberRegisterRequestDTO dto) {
        // 프론트가 뭘 보내든 USER로 강제
        dto.setUserRole("USER");
        memberService.register(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/me")
    public MemberInfoDTO getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인 안 한 경우 자동으로 401
        return new MemberInfoDTO(userDetails.getMember());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {
        try {UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken); // 인증 시도
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 저장

            HttpSession session = request.getSession(true); // 세션 생성
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 세션에 저장

            return ResponseEntity.ok("로그인 성공");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
    }

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

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest dto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        memberService.updatePassword(member.getMid(), dto);
        return ResponseEntity.ok().build();
    }
}