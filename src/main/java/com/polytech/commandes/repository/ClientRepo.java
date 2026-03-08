package com.polytech.commandes.repository;


import com.polytech.commandes.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    public Optional<Client> findByEmail(String email);
    @Query("select c1 from Client c1 where c1.nom like %:txt% or c1.email like %:txt%")
    public  List<Client> search(@Param("txt") String txt);
    public  Client save(Client client);
}
