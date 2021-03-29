package com.kmadrugstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitDTO {
    private int id;
    private String title;
    private String description;
    private BigDecimal price;
    private String photo;

    private List<KitLineDTO> kitLines;
}


