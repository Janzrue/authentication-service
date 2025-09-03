package com.crediya.authenticacion.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private long   expiresIn;
}
