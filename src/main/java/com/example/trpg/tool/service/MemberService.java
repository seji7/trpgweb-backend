package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.MemberRegisterRequestDTO;
import com.example.trpg.tool.dto.MemberUpdateRequest;
import com.example.trpg.tool.security.dto.PasswordUpdateRequest;

public interface MemberService {
    void register(MemberRegisterRequestDTO dto);
    void updateMember(Long mid, MemberUpdateRequest dto);
    void updatePassword(Long mid, PasswordUpdateRequest dto);
}