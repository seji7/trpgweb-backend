package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.AddPlayerRequestDTO;
import com.example.trpg.tool.dto.MemberRegisterRequestDTO;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(MemberRegisterRequestDTO dto) {
        Member member = Member.builder()
                .userId(dto.getUserId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .userAddress(dto.getUserAddress())
                .userPhone(dto.getUserPhone())
                .userRole("USER")
                .nickname(dto.getNickname())
                .build();

        memberRepository.save(member);
    }
}
