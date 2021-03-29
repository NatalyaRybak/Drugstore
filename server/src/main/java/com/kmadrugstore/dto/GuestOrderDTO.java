package com.kmadrugstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestOrderDTO {
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userPhone;
    private String comment;
    private List<OrderLineDTO> orderLines;
}
