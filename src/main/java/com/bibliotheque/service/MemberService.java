package com.bibliotheque.service;

import com.bibliotheque.model.Member;

/**
 * Business operations for library members
 */
public interface MemberService {
    java.util.List<Member> listMembers();
    Member getMember(Integer id) throws com.bibliotheque.exception.NotFoundException;
    void registerMember(Member member) throws com.bibliotheque.exception.ValidationException;
    void updateMember(Member member) throws com.bibliotheque.exception.ValidationException, com.bibliotheque.exception.NotFoundException;
    void removeMember(Integer id) throws com.bibliotheque.exception.NotFoundException;
}
