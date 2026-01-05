package com.bibliotheque.dao;

import java.util.List;

import com.bibliotheque.model.Emprunt;

/**
 * DAO interface for Loan entity
 */
public interface EmpruntDAO extends GenericDAO<Emprunt, Long> {
    List<Emprunt> findByMemberId(Long memberId);
    List<Emprunt> findActiveByBookIsbn(String isbn);
   
    int countEmpruntsEnCours(long idMembre);
    List<Emprunt> findEnCours();
    void save(Emprunt e);
    

}
