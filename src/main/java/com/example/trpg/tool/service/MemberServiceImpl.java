package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.member.MemberRegisterRequestDTO;
import com.example.trpg.tool.dto.member.MemberUpdateRequest;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.exception.CustomException;
import com.example.trpg.tool.repository.MemberRepository;
import com.example.trpg.tool.security.dto.PasswordUpdateRequest;
import com.example.trpg.tool.exception.ErrorCode;
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
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 아이디가 바뀌었는지 확인 후 중복 검사
        if (!member.getUserId().equals(dto.getUserId())) {
            if (memberRepository.existsByUserId(dto.getUserId())) {
                throw new CustomException(ErrorCode.USERID_DUPLICATED);
            }
            member.setUserId(dto.getUserId());
        }

        member.setUsername(dto.getUsername());
        member.setNickname(dto.getNickname());
        member.setUserAddress(dto.getUserAddress());
        member.setUserPhone(dto.getUserPhone());

        memberRepository.save(member);
    }

    @Override
    public void updatePassword(Long mid, PasswordUpdateRequest dto) {
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        memberRepository.save(member);
    }

    @Override
    public Member findByUserId(String username) {
        return memberRepository.findByUserId(username)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
