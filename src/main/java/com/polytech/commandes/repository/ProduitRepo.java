package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepo extends JpaRepository<Produit, Long> {
    Optional<Produit> findByNom(String nom);
    List<Produit> findByNomContaining(String txt);
    List<Produit> findByStockLessThanEqual(Integer stock);
}
