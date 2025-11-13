package com.goorm.membermanagement.controller;

import com.goorm.membermanagement.dto.RequestDTO;
import com.goorm.membermanagement.dto.ResponseDTO;
import com.goorm.membermanagement.entity.Member;
import com.goorm.membermanagement.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody RequestDTO request, HttpSession session) {
        Member member = memberService.login(request.getUsername(), request.getPassword());
        if (member != null) {
            session.setAttribute("member", member);
            return new ResponseDTO("success", "로그인 성공", member.getUsername());
        }
        return new ResponseDTO("fail", "아이디 또는 비밀번호 오류", null);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseDTO logout(HttpSession session) {
        Member current = (Member) session.getAttribute("member");
        if (current == null) {
            return new ResponseDTO("fail", "로그인 상태가 아닙니다", null);
        }

        session.invalidate(); // 세션 완전 초기화
        return new ResponseDTO("success", "로그아웃 성공", null);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseDTO register(@RequestBody RequestDTO request) {
        boolean result = memberService.register(request);
        return result
                ? new ResponseDTO("success", "회원가입 성공", null)
                : new ResponseDTO("fail", "회원가입 실패", null);
    }

    // 정보 수정
    @PutMapping("/update")
    public ResponseDTO update(@RequestBody RequestDTO request, HttpSession session) {
        Member current = (Member) session.getAttribute("member");
        if (current == null) return new ResponseDTO("fail", "로그인 필요", null);

        boolean result = memberService.update(current.getId(), request);
        return result
                ? new ResponseDTO("success", "수정 성공", null)
                : new ResponseDTO("fail", "수정 실패", null);
    }

    // 회원 삭제
    @DeleteMapping("/delete")
    public ResponseDTO delete(HttpSession session) {
        Member current = (Member) session.getAttribute("member");
        if (current == null) return new ResponseDTO("fail", "로그인 필요", null);

        boolean result = memberService.delete(current.getId());
        if (result) session.invalidate();
        return result
                ? new ResponseDTO("success", "삭제 성공", null)
                : new ResponseDTO("fail", "삭제 실패", null);
    }
}
