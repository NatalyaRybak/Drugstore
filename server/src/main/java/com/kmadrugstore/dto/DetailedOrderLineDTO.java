package com.kmadrugstore.dto;

import com.kmadrugstore.entity.Good;
import com.kmadrugstore.entity.ShortGood;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailedOrderLineDTO {
    private GoodDTO good;
    private int amount;
    private double sum;
}