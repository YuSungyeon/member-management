package com.goorm.membermanagement.controller;

import com.goorm.membermanagement.dto.RequestDTO;
import com.goorm.membermanagement.dto.ResponseDTO;
import com.goorm.membermanagement.entity.Member;
import com.goorm.membermanagement.repository.FakeMemberRepository;
import com.goorm.membermanagement.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberControllerTest {

    private MemberController memberController;
    private MemberService memberService;
    private FakeMemberRepository fakeMemberRepository;

    @BeforeEach
    void setUp() {
        fakeMemberRepository = new FakeMemberRepository();
        memberService = new MemberService(fakeMemberRepository);
        memberController = new MemberController(memberService);
    }

    private Member setupTestMember() {
        RequestDTO request = new RequestDTO();
        request.setUsername("testuser");
        request.setPassword("password");
        memberController.register(request);
        return fakeMemberRepository.findByUsernameAndPassword("testuser", "password");
    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() {
        // given
        RequestDTO request = new RequestDTO();
        request.setUsername("testuser");
        request.setPassword("password");

        // when
        ResponseDTO response = memberController.register(request);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("회원가입 성공");
        assertThat(fakeMemberRepository.findByUsernameAndPassword("testuser", "password")).isNotNull();
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        setupTestMember();
        RequestDTO loginRequest = new RequestDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        HttpSession session = new MockHttpSession();

        // when
        ResponseDTO response = memberController.login(loginRequest, session);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("로그인 성공");
        assertThat(session.getAttribute("member")).isNotNull();
        Member sessionMember = (Member) session.getAttribute("member");
        assertThat(sessionMember.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrong_password() {
        // given
        setupTestMember();
        RequestDTO loginRequest = new RequestDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");
        HttpSession session = new MockHttpSession();

        // when
        ResponseDTO response = memberController.login(loginRequest, session);

        // then
        assertThat(response.getStatus()).isEqualTo("fail");
        assertThat(response.getMessage()).isEqualTo("아이디 또는 비밀번호 오류");
        assertThat(session.getAttribute("member")).isNull();
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        // given
        Member member = setupTestMember();
        HttpSession session = new MockHttpSession();
        session.setAttribute("member", member);

        // when
        ResponseDTO response = memberController.logout(session);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("로그아웃 성공");
        assertThatThrownBy(() -> session.getAttribute("member")).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("정보 수정 성공")
    void update_success() {
        // given
        Member member = setupTestMember();
        HttpSession session = new MockHttpSession();
        session.setAttribute("member", member);

        RequestDTO updateRequest = new RequestDTO();
        updateRequest.setPassword("newpassword");

        // when
        ResponseDTO response = memberController.update(updateRequest, session);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("수정 성공");
        assertThat(fakeMemberRepository.findByUsernameAndPassword("testuser", "newpassword")).isNotNull();
        assertThat(fakeMemberRepository.findByUsernameAndPassword("testuser", "password")).isNull();
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void delete_success() {
        // given
        Member member = setupTestMember();
        HttpSession session = new MockHttpSession();
        session.setAttribute("member", member);

        // when
        ResponseDTO response = memberController.delete(session);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("삭제 성공");
        assertThat(fakeMemberRepository.findById(member.getId())).isNull();
        assertThatThrownBy(() -> session.getAttribute("member")).isInstanceOf(IllegalStateException.class);
    }
}