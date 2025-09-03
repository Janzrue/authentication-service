package com.crediya.authenticacion.model.tokeninfo;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenInfo {

    private String tokenType;      // "Bearer"
    private String accessToken;
    private String refreshToken;
    private long   expiresIn;      // segundos (del access token)
}
