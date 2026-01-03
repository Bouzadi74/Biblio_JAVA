package com.bibliotheque.dao;

import java.util.List;

import com.bibliotheque.model.Emprunt;

/**
 * DAO interface for Loan entity
 */
public interface EmpruntDAO extends GenericDAO<Emprunt, Integer> {
    List<Emprunt> findByMemberId(Integer memberId);
    List<Emprunt> findActiveByBookId(Integer bookId);
   
    int countEmpruntsEnCours(int idMembre);
    List<Emprunt> findEnCours();
    void save(Emprunt e);
    

}
