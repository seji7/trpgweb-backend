package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.member.MemberRegisterRequestDTO;
import com.example.trpg.tool.dto.member.MemberUpdateRequest;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.security.dto.PasswordUpdateRequest;

public interface MemberService {
    void register(MemberRegisterRequestDTO dto);
    void updateMember(Long mid, MemberUpdateRequest dto);
    void updatePassword(Long mid, PasswordUpdateRequest dto);

    Member findByUserId(String username);
}