package com.polytech.commandes.service.impl;

import com.polytech.commandes.entity.Commande;
import com.polytech.commandes.entity.Client;
import com.polytech.commandes.entity.LigneCommande;
import com.polytech.commandes.entity.Produit;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.repository.CommandeRepo;
import com.polytech.commandes.repository.ClientRepo;
import com.polytech.commandes.repository.ProduitRepo;
import com.polytech.commandes.service.CommandeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {
    private final CommandeRepo commandeRepo;
    private final ClientRepo clientRepo;
    private final ProduitRepo produitRepo;

    public CommandeServiceImpl(CommandeRepo commandeRepo, ClientRepo clientRepo, ProduitRepo produitRepo) {
        this.commandeRepo = commandeRepo;
        this.clientRepo = clientRepo;
        this.produitRepo = produitRepo;
    }

    @Override
    public List<Commande> findAll() {
        return commandeRepo.findAll();
    }

    @Override
    public Optional<Commande> findById(Long id) {
        return commandeRepo.findById(id);
    }

    @Override
    public Commande create(Commande commande) {
        if (commande.getClient() == null || commande.getClient().getId() == null) {
            throw new BoutiqueAPIException(450, "Client id obligatoire");
        }
        Client client = clientRepo.findById(commande.getClient().getId())
                .orElseThrow(() -> new BoutiqueAPIException(404, "Client non trouvé"));
        commande.setClient(client);

        if (commande.getLignes() == null || commande.getLignes().isEmpty()) {
            throw new BoutiqueAPIException(450, "La commande doit contenir au moins une ligne");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (LigneCommande ligne : commande.getLignes()) {
            if (ligne.getProduit() == null || ligne.getProduit().getId() == null) {
                throw new BoutiqueAPIException(450, "Produit id obligatoire pour chaque ligne");
            }
            Produit produit = produitRepo.findById(ligne.getProduit().getId())
                    .orElseThrow(() -> new BoutiqueAPIException(404, "Produit non trouvé"));
            if (ligne.getQuantite() == null || ligne.getQuantite() <= 0) {
                throw new BoutiqueAPIException(450, "Quantité invalide pour le produit " + produit.getNom());
            }
            if (produit.getStock() < ligne.getQuantite()) {
                throw new BoutiqueAPIException(400, "Stock insuffisant pour produit " + produit.getNom());
            }
            ligne.setPrixUnitaire(produit.getPrix());
            ligne.setCommande(commande);
            total = total.add(produit.getPrix().multiply(BigDecimal.valueOf(ligne.getQuantite())));
        }

        commande.setTotal(total);
        if (commande.getStatut() == null) commande.setStatut(Commande.Status.CREATED);
        if (commande.getDateCommande() == null) commande.setDateCommande(LocalDateTime.now());

        Commande saved = commandeRepo.save(commande);

        // If already validated on creation, update stock
        if (saved.getStatut() == Commande.Status.VALIDATED) {
            for (LigneCommande ligne : saved.getLignes()) {
                Produit p = produitRepo.findById(ligne.getProduit().getId()).orElseThrow();
                p.setStock(p.getStock() - ligne.getQuantite());
                produitRepo.save(p);
            }
        }

        return saved;
    }

    @Override
    public Commande update(Commande commande) {
        if (commande.getId() == null) throw new BoutiqueAPIException(450, "id commande obligatoire");
        Commande existing = commandeRepo.findById(commande.getId())
                .orElseThrow(() -> new BoutiqueAPIException(404, "Commande non trouvée"));

        if (commande.getClient() != null && commande.getClient().getId() != null) {
            Client client = clientRepo.findById(commande.getClient().getId())
                    .orElseThrow(() -> new BoutiqueAPIException(404, "Client non trouvé"));
            commande.setClient(client);
        } else {
            commande.setClient(existing.getClient());
        }

        // Recalculate lines/prices/total
        BigDecimal total = BigDecimal.ZERO;
        if (commande.getLignes() != null) {
            for (LigneCommande ligne : commande.getLignes()) {
                if (ligne.getProduit() == null || ligne.getProduit().getId() == null) {
                    throw new BoutiqueAPIException(450, "Produit id obligatoire pour chaque ligne");
                }
                Produit produit = produitRepo.findById(ligne.getProduit().getId())
                        .orElseThrow(() -> new BoutiqueAPIException(404, "Produit non trouvé"));
                if (ligne.getQuantite() == null || ligne.getQuantite() <= 0) {
                    throw new BoutiqueAPIException(450, "Quantité invalide pour le produit " + produit.getNom());
                }
                ligne.setPrixUnitaire(produit.getPrix());
                ligne.setCommande(commande);
                total = total.add(produit.getPrix().multiply(BigDecimal.valueOf(ligne.getQuantite())));
            }
            commande.setTotal(total);
        } else {
            commande.setTotal(existing.getTotal());
        }

        // If changing to VALIDATED, ensure stock and decrement
        boolean goingToValidated = commande.getStatut() == Commande.Status.VALIDATED && existing.getStatut() != Commande.Status.VALIDATED;
        if (goingToValidated) {
            for (LigneCommande ligne : commande.getLignes()) {
                Produit produit = produitRepo.findById(ligne.getProduit().getId())
                        .orElseThrow(() -> new BoutiqueAPIException(404, "Produit non trouvé"));
                if (produit.getStock() < ligne.getQuantite()) {
                    throw new BoutiqueAPIException(400, "Stock insuffisant pour produit " + produit.getNom());
                }
            }
            // decrement stock
            for (LigneCommande ligne : commande.getLignes()) {
                Produit produit = produitRepo.findById(ligne.getProduit().getId()).orElseThrow();
                produit.setStock(produit.getStock() - ligne.getQuantite());
                produitRepo.save(produit);
            }
        }

        return commandeRepo.save(commande);
    }

    @Override
    public void delete(Long id) {
        commandeRepo.deleteById(id);
    }

    @Override
    public List<Commande> findByClient(Long clientId) {
        return commandeRepo.findByClientId(clientId);
    }
}
