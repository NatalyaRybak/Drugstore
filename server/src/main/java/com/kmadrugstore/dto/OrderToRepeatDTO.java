package com.kmadrugstore.dto;

import com.kmadrugstore.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderToRepeatDTO {
    private int orderId;
    private int bonuses;
    private String comment;
}