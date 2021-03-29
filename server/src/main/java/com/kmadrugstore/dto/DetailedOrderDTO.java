package com.kmadrugstore.dto;

import com.kmadrugstore.entity.OrderStatus;
import com.kmadrugstore.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailedOrderDTO {
    private int id;
    private LocalDateTime date;
    private BigDecimal total;
    private int bonusesToUse;
    private int bonusesToGet;
    private String phone;
    private String comment;
    private OrderStatusNameDTO status;
    private UserDTO user;
    private List<DetailedOrderLineDTO> orderLines;
}