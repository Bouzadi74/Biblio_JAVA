package com.bibliotheque.dao;

import com.bibliotheque.model.Member;
import java.util.List;
import java.util.Optional;

/**
 * Concrete MemberDAO implementation (persistence omitted)
 */
public class MemberDAOImpl implements MemberDAO {
    public MemberDAOImpl(com.bibliotheque.infra.DatabaseConnection db) {}

    public List<Member> findAll() { return java.util.Collections.emptyList(); }
    public Optional<Member> findById(Integer id) { return Optional.empty(); }
    public void insert(Member entity) {}
    public void update(Member entity) {}
    public void delete(Integer id) {}
    public Optional<Member> findByEmail(String email) { return Optional.empty(); }
}
