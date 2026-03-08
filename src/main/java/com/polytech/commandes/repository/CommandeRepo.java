package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommandeRepo extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);
}
