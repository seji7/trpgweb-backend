package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.RoomRegisterRequestDTO;
import com.example.trpg.tool.dto.RoomResponseDTO;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.repository.MemberRepository;
import com.example.trpg.tool.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RoomControllerTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
}