package com.polytech.commandes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class Produit {
    @Id @GeneratedValue
    private Long id;
    private String nom;
    private BigDecimal prix;
    private Integer stock;
}
