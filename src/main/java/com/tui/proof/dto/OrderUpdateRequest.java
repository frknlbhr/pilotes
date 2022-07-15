package com.tui.proof.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class OrderUpdateRequest {

    @NotNull
    private Long orderNumber;
    private PilotesPortionEnum portion;
    @Valid
    private AddressDto deliveryAddress;

}
