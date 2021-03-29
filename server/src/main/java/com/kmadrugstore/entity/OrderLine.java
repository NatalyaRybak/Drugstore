package com.kmadrugstore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_line")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "n_in_order")
    private int numInOrder;
    private BigDecimal sum;
    @ManyToOne
    @JoinColumn(name = "good_id", nullable = false)
    private Good good;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
