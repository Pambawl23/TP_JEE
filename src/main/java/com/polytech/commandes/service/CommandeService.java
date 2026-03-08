package com.polytech.commandes.service;

import com.polytech.commandes.entity.Commande;

import java.util.List;
import java.util.Optional;

public interface CommandeService {
    List<Commande> findAll();
    Optional<Commande> findById(Long id);
    Commande create(Commande commande);
    Commande update(Commande commande);
    void delete(Long id);
    List<Commande> findByClient(Long clientId);
}
