package com.polytech.commandes.controller;

import com.polytech.commandes.entity.LigneCommande;
import com.polytech.commandes.repository.LigneCommandeRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Lister les lignes", responses = @ApiResponse(responseCode = "200", description = "Liste des lignes"))
    public List<LigneCommande> findAll() {
        return ligneRepo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ligne par ID", responses = @ApiResponse(responseCode = "200", description = "Ligne trouvée"))
    public ResponseEntity<LigneCommande> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ligneRepo.findById(id).orElse(null));
    }

    @PostMapping
    @Operation(summary = "Créer une ligne", responses = {
            @ApiResponse(responseCode = "201", description = "Ligne créée"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    public ResponseEntity<?> create(@RequestBody LigneCommande ligne) {
        if (ligne.getId() != null) return ResponseEntity.status(201).body(java.util.Map.of("error", "Id non autorisé"));
        return ResponseEntity.status(201).body(ligneRepo.save(ligne));
    }

    @PutMapping
    @Operation(summary = "Mettre à jour une ligne", responses = @ApiResponse(responseCode = "200", description = "Ligne mise à jour"))
    public ResponseEntity<?> update(@RequestBody LigneCommande ligne) {
        if (ligne.getId() == null) return ResponseEntity.ok(java.util.Map.of("error", "Id requis"));
        if (!ligneRepo.existsById(ligne.getId())) return ResponseEntity.ok(java.util.Map.of("error", "Ligne introuvable"));
        return ResponseEntity.ok(ligneRepo.save(ligne));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une ligne", responses = @ApiResponse(responseCode = "204", description = "Ligne supprimée"))
    public ResponseEntity delete(@PathVariable Long id) {
        ligneRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
