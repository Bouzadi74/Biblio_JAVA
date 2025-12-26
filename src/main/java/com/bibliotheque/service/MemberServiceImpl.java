package com.bibliotheque.service;

import com.bibliotheque.dao.MemberDAO;
import com.bibliotheque.model.Member;

/**
 * Member service implementation skeleton. Business rules to be implemented by students.
 */
public class MemberServiceImpl implements MemberService {
    private final MemberDAO memberDAO;

    public MemberServiceImpl(MemberDAO memberDAO) { this.memberDAO = memberDAO; }

    public java.util.List<Member> listMembers() { return java.util.Collections.emptyList(); }
    public Member getMember(Integer id) throws com.bibliotheque.exception.NotFoundException { return null; }
    public void registerMember(Member member) throws com.bibliotheque.exception.ValidationException {}
    public void updateMember(Member member) throws com.bibliotheque.exception.ValidationException, com.bibliotheque.exception.NotFoundException {}
    public void removeMember(Integer id) throws com.bibliotheque.exception.NotFoundException {}
}
