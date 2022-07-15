package com.tui.proof.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterResponse {

    private OrderDto order;
    private ClientDto client;

}
