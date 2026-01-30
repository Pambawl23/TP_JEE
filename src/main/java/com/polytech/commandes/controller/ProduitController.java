package com.polytech.commandes.controller;

import com.polytech.commandes.entity.Produit;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.service.ProduitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/c1/produits")
public class ProduitController {
    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    @Operation(
            summary = "Lister tous les produits",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des produits")
            }
    )
    public List<Produit> findAll(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isBlank()) return produitService.searchByName(search);
        return produitService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer un produit par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produit trouvé"),
                    @ApiResponse(responseCode = "404", description = "Produit non trouvé")
            }
    )
    public ResponseEntity<Produit> findById(@PathVariable Long id) {
        return produitService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un produit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    public ResponseEntity<Produit> create(@RequestBody Produit produit) {
        try {
            Produit p = produitService.create(produit);
            return ResponseEntity.status(201).body(p);
        } catch (BoutiqueAPIException e) {
            return (ResponseEntity<Produit>) e.getResponse();
        }
    }

    @PutMapping
    @Operation(
            summary = "Mettre à jour un produit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produit mis à jour"),
                    @ApiResponse(responseCode = "400", description = "Erreur de validation")
            }
    )
    public ResponseEntity update(@RequestBody Produit produit) {
        try {
            Produit p = produitService.update(produit);
            return ResponseEntity.ok(p);
        } catch (BoutiqueAPIException e) {
            return e.getResponse();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un produit",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produit supprimé")
            }
    )
    public ResponseEntity delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
