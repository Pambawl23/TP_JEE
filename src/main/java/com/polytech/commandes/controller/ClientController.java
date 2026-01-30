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
import java.util.Optional;

@RestController
@RequestMapping("/api/c1/clients")
public class ClientController {
    private final ClientService clientService;
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    //   http://localhost:8080/api/c1/clients
    @Operation(
            summary = "liste des personnes",
            description = "Retourne la liste de toutes les clients",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La liste des clients est retournée"
                    )
            }
    )
    @GetMapping
    public List<Client> findAll(@RequestParam(name = "search", required = false) String searchTxt) {
        if(searchTxt != null && !searchTxt.isEmpty()) {
            return clientService.search(searchTxt);
        }
        return clientService.findAll();
    }
        @GetMapping("/{id}")
        @Operation(
                summary = "Récupérer un client par ID",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Client trouvé"),
                        @ApiResponse(responseCode = "414", description = "Client non trouvé")
                }
        )
        public ResponseEntity<Client> findById(@PathVariable("id") Long id) {
                Optional<Client> clientdb = clientService.findById(id);
                if (clientdb.isPresent()) {
                        Client client = clientdb.get();
                        return ResponseEntity.ok().body(client);
                }
                return ResponseEntity.status(414).build();
        }

    @Operation(
            summary = "Enregistre nouvelle personne",
            description = "Permet d'enregistrer une nouvelle personne,Id ne doit pas etre renseigner",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "La personne est enregsitrée avec succés",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "  \"id\": 3" +
                                                    "  \"nom\": \"FALL\"" +
                                                    "  \"email\": \"fall@gmail.com\"" +
                                                    "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "451",
                            description = "Le prenom n'a pas ete renseigne"
                    ),
                    @ApiResponse(
                            responseCode = "452",
                            description = "Le nom n'a pas ete renseigne"
                    )
            }
    )
    @PostMapping
    public ResponseEntity createPersonne(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Un client  à créer",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{" +
                                                    "\"nom\": \"FALL\"," +
                                                    "\"email\": \"fall@gmail.com\"" +
                                                    "}"
                                    )
                            }
                    )
            )
            @RequestBody Client client) {
        try {
            Client p = clientService.create(client);
            return ResponseEntity.status(201).body(p);
        } catch (BoutiqueAPIException e) {
            return e.getResponse();
        }
    }

    @PutMapping
    @Operation(
            summary = "Mettre à jour un client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client mis à jour")
            }
    )
    public ResponseEntity<Client> update(@RequestBody Client client) {
        Client updated = clientService.update(client);
        return ResponseEntity.ok(updated);
    }
}