package com.tui.proof.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {
    private String name;
    private String surname;
    private String username;
    private String email;
}
