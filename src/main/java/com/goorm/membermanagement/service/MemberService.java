package com.goorm.membermanagement.service;

import com.goorm.membermanagement.dao.MemberDao;
import com.goorm.membermanagement.dto.RequestDTO;
import com.goorm.membermanagement.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    // 로그인
    public Member login(String username, String password) {
        return memberDao.findByUsernameAndPassword(username, password);
    }

    // 회원가입
    @Transactional
    public boolean register(RequestDTO request) {
        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setPassword(request.getPassword());
        memberDao.save(member);
        return true;
    }

    // 정보 수정 (현재는 password만 수정 가능)
    @Transactional
    public boolean update(Long id, RequestDTO request) {
        Member member = new Member();
        member.setId(id);
        member.setPassword(request.getPassword());
        return memberDao.update(member);
    }

    // 회원 삭제
    @Transactional
    public boolean delete(Long id) {
        return memberDao.delete(id);
    }
}
