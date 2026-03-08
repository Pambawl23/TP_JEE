package com.polytech.commandes.service;

import com.polytech.commandes.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAll();
    Optional<Client> findById(Long id);
    Client create(Client client);
    Client update(Client client);
    List<Client> search(String txt);
}
