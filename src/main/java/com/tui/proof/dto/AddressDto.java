package com.tui.proof.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AddressDto {
    @NotBlank
    private String street;
    private String postcode;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
}
