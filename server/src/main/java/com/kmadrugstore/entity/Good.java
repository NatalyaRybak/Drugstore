package com.kmadrugstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "good")
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String manufacturer;
    private String country;
    private BigDecimal price;
    @Column(name = "n_available")
    private int numAvailable;
    @Column(name = "n_in_pack")
    private String numInPack;
    private String dose;
    @Column(name = "is_prescription_needed")
    private boolean isPrescriptionNeeded;
    private String form;
    private String description;
    @Column(name = "shelf_life")
    private String shelfLife;
    private String photo;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToMany
    @JoinTable(
            name = "active_component_good",
            joinColumns = @JoinColumn(name = "good_id"),
            inverseJoinColumns = @JoinColumn(name = "active_component_id"))
    Set<ActiveComponent> activeComponents;

    public Good(final int id) {
        this.id = id;
    }
}
