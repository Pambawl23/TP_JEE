package com.polytech.commandes.service.impl;

import com.polytech.commandes.entity.Produit;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.repository.ProduitRepo;
import com.polytech.commandes.service.ProduitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitServiceImpl implements ProduitService {
    private final ProduitRepo produitRepo;

    public ProduitServiceImpl(ProduitRepo produitRepo) {
        this.produitRepo = produitRepo;
    }

    @Override
    public List<Produit> findAll() {
        return produitRepo.findAll();
    }

    @Override
    public Optional<Produit> findById(Long id) {
        return produitRepo.findById(id);
    }

    @Override
    public Produit create(Produit produit) {
        if (produit.getId() != null) throw new BoutiqueAPIException(450, "Id non autorisé à la création");
        return produitRepo.save(produit);
    }

    @Override
    public Produit update(Produit produit) {
        if (produit.getId() == null) throw new BoutiqueAPIException(450, "Id requis pour la mise à jour");
        Produit existing = produitRepo.findById(produit.getId()).orElseThrow(() -> new BoutiqueAPIException(404, "Produit non trouvé"));
        existing.setNom(produit.getNom());
        existing.setPrix(produit.getPrix());
        existing.setStock(produit.getStock());
        return produitRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        produitRepo.deleteById(id);
    }

    @Override
    public List<Produit> searchByName(String txt) {
        return produitRepo.findByNomContaining(txt);
    }
}
