package com.bibliotheque.dao;

import com.bibliotheque.model.Member;
import java.util.Optional;

/**
 * DAO interface for Member entity
 */
public interface MemberDAO extends GenericDAO<Member, Integer> {
    Optional<Member> findByEmail(String email);
}
