package com.polytech.commandes.controller;

import com.polytech.commandes.entity.Commande;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping
    @Operation(
            summary = "Lister toutes les commandes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des commandes")
            }
    )
    public List<Commande> findAll() {
        return commandeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer une commande par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Commande trouvée"),
                    @ApiResponse(responseCode = "404", description = "Commande non trouvée")
            }
    )
    public ResponseEntity<Commande> findById(@PathVariable Long id) {
        return commandeService.findById(id)
                .map(c -> ResponseEntity.ok().body(c))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    @Operation(
            summary = "Lister les commandes d'un client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des commandes du client")
            }
    )
    public List<Commande> findByClient(@PathVariable Long clientId) {
        return commandeService.findByClient(clientId);
    }

    @PostMapping
    @Operation(summary = "Créer une commande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commande créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    public ResponseEntity<Commande> create(@RequestBody Commande commande) {
        try {
            Commande c = commandeService.create(commande);
            return ResponseEntity.status(201).body(c);
        } catch (BoutiqueAPIException e) {
            return (ResponseEntity<Commande>) e.getResponse();
        }
    }

    @PutMapping
    @Operation(
            summary = "Mettre à jour une commande",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Commande mise à jour"),
                    @ApiResponse(responseCode = "400", description = "Erreur de validation")
            }
    )
    public ResponseEntity update(@RequestBody Commande commande) {
        try {
            Commande c = commandeService.update(commande);
            return ResponseEntity.ok(c);
        } catch (BoutiqueAPIException e) {
            return e.getResponse();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une commande",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Commande supprimée")
            }
    )
    public ResponseEntity delete(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
