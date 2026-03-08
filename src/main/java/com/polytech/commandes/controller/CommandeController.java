package com.polytech.commandes.controller;

import com.polytech.commandes.entity.Commande;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/c1/commandes")
public class CommandeController {
    private final CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }


    @PostMapping
    @Operation(summary = "Créer une commande", responses = {
            @ApiResponse(responseCode = "201", description = "Commande créée"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    public ResponseEntity<?> create(@RequestBody Commande commande) {
        try {
            return ResponseEntity.status(201).body(commandeService.create(commande));
        } catch (BoutiqueAPIException e) {
            return ResponseEntity.status(201).body(java.util.Map.of("error", e.getMsg()));
        }
    }


    @GetMapping
    @Operation(summary = "Lister les commandes", responses = @ApiResponse(responseCode = "200", description = "Liste des commandes"))
    public List<Commande> findAll() {
        return commandeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Commande par ID", responses = @ApiResponse(responseCode = "200", description = "Commande trouvée"))
    public ResponseEntity<Commande> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.findById(id).orElse(null));
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Commandes d'un client", responses = @ApiResponse(responseCode = "200", description = "Liste des commandes"))
    public List<Commande> findByClient(@PathVariable Long clientId) {
        return commandeService.findByClient(clientId);
    }


    @PutMapping
    @Operation(summary = "Mettre à jour une commande", responses = @ApiResponse(responseCode = "200", description = "Commande mise à jour"))
    public ResponseEntity<?> update(@RequestBody Commande commande) {
        try {
            return ResponseEntity.ok(commandeService.update(commande));
        } catch (BoutiqueAPIException e) {
            return ResponseEntity.ok(java.util.Map.of("error", e.getMsg()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une commande", responses = @ApiResponse(responseCode = "204", description = "Commande supprimée"))
    public ResponseEntity delete(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
