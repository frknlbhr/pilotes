package com.tui.proof.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {
    private Long orderNumber;
    private AddressDto deliveryAddress;
    private PilotesPortionEnum portion;
    private Double orderTotal;
}
