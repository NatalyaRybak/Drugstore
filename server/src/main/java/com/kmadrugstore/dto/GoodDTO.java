package com.kmadrugstore.dto;

import com.kmadrugstore.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodDTO {
    private int id;
    private String name;
    private BigDecimal price;
    private String numInPack;
    private String dose;
    private boolean isPrescriptionNeeded;
    private String form;
    private String photo;
}