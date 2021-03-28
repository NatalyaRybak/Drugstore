package com.kmadrugstore.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GoodFilter {

    private String search;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private String manufacturer;
    private String country;
    private Integer categoryId;
    private Integer activeComponentId;

}
