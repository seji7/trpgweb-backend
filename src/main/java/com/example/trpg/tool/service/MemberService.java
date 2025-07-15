package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.MemberRegisterRequestDTO;
import com.example.trpg.tool.dto.MemberUpdateRequest;

public interface MemberService {
    void register(MemberRegisterRequestDTO dto);
    void updateMember(Long mid, MemberUpdateRequest dto);
}