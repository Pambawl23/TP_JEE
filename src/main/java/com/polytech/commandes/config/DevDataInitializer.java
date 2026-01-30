package com.polytech.commandes.config;

import com.polytech.commandes.entity.Client;
import com.polytech.commandes.entity.Commande;
import com.polytech.commandes.entity.LigneCommande;
import com.polytech.commandes.entity.Produit;
import com.polytech.commandes.repository.ClientRepo;
import com.polytech.commandes.repository.ProduitRepo;
import com.polytech.commandes.service.CommandeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {
    private final ClientRepo clientRepo;
    private final ProduitRepo produitRepo;
    private final CommandeService commandeService;

    public DevDataInitializer(ClientRepo clientRepo, ProduitRepo produitRepo, CommandeService commandeService) {
        this.clientRepo = clientRepo;
        this.produitRepo = produitRepo;
        this.commandeService = commandeService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (clientRepo.count() == 0) {
            Client cl1 = new Client();
            cl1.setNom("Dupont");
            cl1.setEmail("dupont@example.com");
            clientRepo.save(cl1);
            Client cl2 = new Client();
            cl2.setNom("Durand");
            cl2.setEmail("durand@example.com");
            clientRepo.save(cl2);
        }

        if (produitRepo.count() == 0) {
            produitRepo.save(createProduit("Stylo", "1.50", 100));
            produitRepo.save(createProduit("Cahier", "3.20", 50));
            produitRepo.save(createProduit("Ordinateur", "999.99", 5));
        }

        // create a complete commande for first client with validated status to test stock update
        List<Client> clients = clientRepo.findAll();
        if (!clients.isEmpty()) {
            Client c = clients.get(0);

            Commande commande = new Commande();
            commande.setClient(c);
            commande.setStatut(Commande.Status.VALIDATED);

            List<LigneCommande> lignes = new ArrayList<>();

            Produit p1 = produitRepo.findByNom("Stylo").orElse(null);
            Produit p2 = produitRepo.findByNom("Cahier").orElse(null);
            if (p1 != null) {
                LigneCommande l1 = new LigneCommande();
                l1.setProduit(p1);
                l1.setQuantite(2);
                lignes.add(l1);
            }
            if (p2 != null) {
                LigneCommande l2 = new LigneCommande();
                l2.setProduit(p2);
                l2.setQuantite(1);
                lignes.add(l2);
            }
            if (!lignes.isEmpty()) {
                commande.setLignes(lignes);
                try {
                    commandeService.create(commande);
                } catch (Exception e) {
                    System.out.println("Dev initializer: unable to create commande: " + e.getMessage());
                }
            }
        }
    }

    private Produit createProduit(String nom, String prix, int stock) {
        Produit p = new Produit();
        p.setNom(nom);
        p.setPrix(new BigDecimal(prix));
        p.setStock(stock);
        return p;
    }
}
