package com.polytech.commandes.service.impl;

import com.polytech.commandes.entity.Client;
import com.polytech.commandes.exception.BoutiqueAPIException;
import com.polytech.commandes.repository.ClientRepo;
import com.polytech.commandes.service.ClientService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;



@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepository;
    public ClientServiceImpl(ClientRepo clientRepository) {
        this.clientRepository = clientRepository;
    }
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client create(Client client) throws BoutiqueAPIException {
        if (client.getNom() == null || client.getNom().isBlank()) {
            throw new BoutiqueAPIException(451,"le nom est obligatoire");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new BoutiqueAPIException(450,"email est obligatoire");
        }

        String email = client.getEmail().trim();
        Optional<Client> personneOptional = clientRepository.findByEmail(client.getEmail());
        if (personneOptional.isPresent()) {
            throw new BoutiqueAPIException(409,"L'adresse email '"+email+"' existe deja");
        }
        return clientRepository.save(client);
    }
    @Override
    public Client update(Client client) {
        return clientRepository.save(client);
    }
    @Override
    public List<Client> search(String txt){
        return clientRepository.search(txt);
    }
}


