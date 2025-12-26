package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;

import java.util.List;

import com.bibliotheque.model.Emprunt;

/**
 * DAO interface for Loan entity
 */
public interface EmpruntDAO extends GenericDAO<Emprunt, Integer> {
    List<Emprunt> findByMemberId(Integer memberId);
    List<Emprunt> findActiveByBookId(Integer bookId);
}
