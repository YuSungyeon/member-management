package com.goorm.membermanagement.repository;

import com.goorm.membermanagement.dao.MemberDao;
import com.goorm.membermanagement.entity.Member;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberRepository implements MemberDao {

    private final Map<Long, Member> data = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Member findById(Long id) {
        return data.get(id);
    }

    @Override
    public Member findByUsernameAndPassword(String username, String password) {
        return data.values().stream()
                .filter(member -> member.getUsername().equals(username) && member.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Member save(Member member) {
        if (member.getId() == null) {
            member.setId(idGenerator.incrementAndGet());
        }
        data.put(member.getId(), member);
        return member;
    }

    @Override
    public boolean update(Member member) {
        if (data.containsKey(member.getId())) {
            data.put(member.getId(), member);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return data.remove(id) != null;
    }
}
