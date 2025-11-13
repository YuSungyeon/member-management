package com.goorm.membermanagement.dao;

import com.goorm.membermanagement.entity.Member;

public interface MemberDao {
    Member findByUsernameAndPassword(String username, String password);
    Member save(Member member);
    boolean update(Member member);
    boolean delete(Long id);
}
