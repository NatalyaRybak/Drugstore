package com.kmadrugstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderDTO {
    private int bonusesToUse;
    private String userPhone;
    private String comment;
    private List<OrderLineDTO> orderLines;
}
