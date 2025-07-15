package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.MemberRegisterRequestDTO;
import com.example.trpg.tool.dto.MemberUpdateRequest;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
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

    @Override
    public void updateMember(Long mid, MemberUpdateRequest dto) {
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setUsername(dto.getUsername());
        member.setNickname(dto.getNickname());
        member.setUserAddress(dto.getUserAddress());
        member.setUserPhone(dto.getUserPhone());

        memberRepository.save(member);
    }
}
