package com.goorm.membermanagement.service;

import com.goorm.membermanagement.dto.RequestDTO;
import com.goorm.membermanagement.entity.Member;
import com.goorm.membermanagement.repository.FakeMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceTest {

    private MemberService memberService;
    private FakeMemberRepository fakeMemberRepository;

    @BeforeEach
    void setUp() {
        fakeMemberRepository = new FakeMemberRepository();
        memberService = new MemberService(fakeMemberRepository);
    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() {
        // given
        RequestDTO request = new RequestDTO();
        request.setUsername("testuser");
        request.setPassword("password");

        // when
        boolean result = memberService.register(request);

        // then
        assertThat(result).isTrue();
        Member savedMember = fakeMemberRepository.findByUsernameAndPassword("testuser", "password");
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        RequestDTO registerRequest = new RequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        memberService.register(registerRequest);

        // when
        Member loggedInMember = memberService.login("testuser", "password");

        // then
        assertThat(loggedInMember).isNotNull();
        assertThat(loggedInMember.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrong_password() {
        // given
        RequestDTO registerRequest = new RequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        memberService.register(registerRequest);

        // when
        Member loggedInMember = memberService.login("testuser", "wrongpassword");

        // then
        assertThat(loggedInMember).isNull();
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 아이디")
    void login_fail_non_existent_username() {
        // when
        Member loggedInMember = memberService.login("nonexistent", "password");

        // then
        assertThat(loggedInMember).isNull();
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void update_success() {
        // given
        RequestDTO registerRequest = new RequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        memberService.register(registerRequest);
        Member savedMember = fakeMemberRepository.findByUsernameAndPassword("testuser", "password");

        RequestDTO updateRequest = new RequestDTO();
        updateRequest.setPassword("newpassword");

        // when
        boolean result = memberService.update(savedMember.getId(), updateRequest);

        // then
        assertThat(result).isTrue();
        Member updatedMember = fakeMemberRepository.findByUsernameAndPassword("testuser", "newpassword");
        assertThat(updatedMember).isNotNull();
        // Verify old password no longer works
        Member oldMember = fakeMemberRepository.findByUsernameAndPassword("testuser", "password");
        assertThat(oldMember).isNull();
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void delete_success() {
        // given
        RequestDTO registerRequest = new RequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        memberService.register(registerRequest);
        Member savedMember = fakeMemberRepository.findByUsernameAndPassword("testuser", "password");
        assertThat(savedMember).isNotNull();

        // when
        boolean result = memberService.delete(savedMember.getId());

        // then
        assertThat(result).isTrue();
        Member deletedMember = fakeMemberRepository.findByUsernameAndPassword("testuser", "password");
        assertThat(deletedMember).isNull();
    }
}