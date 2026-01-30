package com.polytech.commandes.controller;

import com.polytech.commandes.entity.LigneCommande;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.repository.LigneCommandeRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/c1/lignes")
public class LigneCommandeController {
    private final LigneCommandeRepo ligneRepo;

    public LigneCommandeController(LigneCommandeRepo ligneRepo) {
        this.ligneRepo = ligneRepo;
    }

    @GetMapping
    @Operation(
            summary = "Lister toutes les lignes de commande",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des lignes de commande")
            }
    )
    public List<LigneCommande> findAll() {
        return ligneRepo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer une ligne de commande par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ligne de commande trouvée"),
                    @ApiResponse(responseCode = "404", description = "Ligne de commande non trouvée")
            }
    )
    public ResponseEntity<LigneCommande> findById(@PathVariable Long id) {
        return ligneRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une ligne de commande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ligne de commande créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    public ResponseEntity<LigneCommande> create(@RequestBody LigneCommande ligne) {
        if (ligne.getId() != null) return ResponseEntity.badRequest().build();
        LigneCommande l = ligneRepo.save(ligne);
        return ResponseEntity.status(201).body(l);
    }

    @PutMapping
    @Operation(
            summary = "Mettre à jour une ligne de commande",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ligne de commande mise à jour"),
                    @ApiResponse(responseCode = "400", description = "ID manquant"),
                    @ApiResponse(responseCode = "404", description = "Ligne de commande non trouvée")
            }
    )
    public ResponseEntity update(@RequestBody LigneCommande ligne) {
        if (ligne.getId() == null) return ResponseEntity.badRequest().build();
        if (!ligneRepo.existsById(ligne.getId())) return ResponseEntity.notFound().build();
        LigneCommande l = ligneRepo.save(ligne);
        return ResponseEntity.ok(l);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une ligne de commande",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ligne de commande supprimée")
            }
    )
    public ResponseEntity delete(@PathVariable Long id) {
        ligneRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
