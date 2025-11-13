package com.goorm.membermanagement.dao;

import com.goorm.membermanagement.entity.Member;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDaoImpl implements MemberDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Member findByUsernameAndPassword(String username, String password) {
        try {
            return em.createQuery(
                            "SELECT m FROM Member m WHERE m.username=:u AND m.password=:p", Member.class)
                    .setParameter("u", username)
                    .setParameter("p", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public boolean update(Member member) {
        em.merge(member);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        Member m = em.find(Member.class, id);
        if (m != null) {
            em.remove(m);
            return true;
        }
        return false;
    }
}
