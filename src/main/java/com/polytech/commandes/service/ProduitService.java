package com.polytech.commandes.service;

import com.polytech.commandes.entity.Produit;

import java.util.List;
import java.util.Optional;

public interface ProduitService {
    List<Produit> findAll();
    Optional<Produit> findById(Long id);
    Produit create(Produit produit);
    Produit update(Produit produit);
    void delete(Long id);
    List<Produit> searchByName(String txt);
}
