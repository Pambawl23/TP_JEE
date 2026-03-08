package com.polytech.commandes.controller;

import com.polytech.commandes.entity.Produit;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.service.ProduitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/c1/produits")
public class ProduitController {
    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping
    @Operation(summary = "Créer un produit", responses = {
            @ApiResponse(responseCode = "201", description = "Produit créé"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    public ResponseEntity<?> create(@RequestBody Produit produit) {
        try {
            return ResponseEntity.status(201).body(produitService.create(produit));
        } catch (BoutiqueAPIException e) {
            return ResponseEntity.status(201).body(java.util.Map.of("error", e.getMsg()));
        }
    }

    @GetMapping
    @Operation(summary = "Lister les produits", responses = @ApiResponse(responseCode = "200", description = "Liste des produits"))
    public List<Produit> findAll(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isBlank()) return produitService.searchByName(search);
        return produitService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Produit par ID", responses = @ApiResponse(responseCode = "200", description = "Produit trouvé"))
    public ResponseEntity<Produit> findById(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.findById(id).orElse(null));
    }


    @PutMapping
    @Operation(summary = "Mettre à jour un produit", responses = @ApiResponse(responseCode = "200", description = "Produit mis à jour"))
    public ResponseEntity<?> update(@RequestBody Produit produit) {
        try {
            return ResponseEntity.ok(produitService.update(produit));
        } catch (BoutiqueAPIException e) {
            return ResponseEntity.ok(java.util.Map.of("error", e.getMsg()));
        }
    }

    public ResponseEntity delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
