package com.kmadrugstore.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "order_date")
    private LocalDateTime date;
    private BigDecimal total;
    @Column(name = "bonuses_to_use")
    private int bonusesToUse;
    @Column(name = "bonuses_to_get")
    private int bonusesToGet;
    @Column(name = "order_expiration_date")
    private LocalDateTime expirationDate;
    private String phone;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus status;

    public Order(final int id) {
        this.id = id;
    }

}
