package com.polytech.commandes.controller;

import com.polytech.commandes.entity.Client;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/c1/clients")
public class ClientController {
    private final ClientService clientService;
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    //   http://localhost:8080/api/c1/clients
    @Operation(summary = "Liste des clients", description = "Token requis : POST /securite/token puis Autoriser.", responses = @ApiResponse(responseCode = "200", description = "Liste des clients"))
    @GetMapping
    public List<Client> findAll(@RequestParam(name = "search", required = false) String searchTxt) {
        if(searchTxt != null && !searchTxt.isEmpty()) {
            return clientService.search(searchTxt);
        }
        return clientService.findAll();
    }
        @GetMapping("/{id}")
        @Operation(summary = "Client par ID", responses = @ApiResponse(responseCode = "200", description = "Client trouvé"))
        public ResponseEntity<Client> findById(@PathVariable("id") Long id) {
                return ResponseEntity.ok(clientService.findById(id).orElse(null));
        }

    @Operation(summary = "Créer un client", responses = {
            @ApiResponse(responseCode = "201", description = "Client créé"),
            @ApiResponse(responseCode = "403", description = "Non autorisé")
    })
    @PostMapping
    public ResponseEntity<?> createPersonne(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Client à créer", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"nom\":\"FALL\",\"email\":\"fall@gmail.com\"}")))
            @RequestBody Client client) {
        try {
            return ResponseEntity.status(201).body(clientService.create(client));
        } catch (BoutiqueAPIException e) {
            return ResponseEntity.status(201).body(java.util.Map.of("error", e.getMsg()));
        }
    }

    @PutMapping
    @Operation(summary = "Mettre à jour un client", responses = @ApiResponse(responseCode = "200", description = "Client mis à jour"))
    public ResponseEntity<Client> update(@RequestBody Client client) {
        Client updated = clientService.update(client);
        return ResponseEntity.ok(updated);
    }
}
