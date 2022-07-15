package com.tui.proof.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class OrderCreateRequest {
    @NotNull
    @Valid
    private AddressDto deliveryAddress;
    @NotNull
    private PilotesPortionEnum portion;

}
