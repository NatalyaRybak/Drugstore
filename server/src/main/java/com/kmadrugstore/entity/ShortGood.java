package com.kmadrugstore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "good")
public class ShortGood {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private BigDecimal price;
    @Column(name = "n_available")
    private int numAvailable;
    @Column(name = "n_in_pack")
    private String numInPack;
    private String dose;
    private String photo;
}

