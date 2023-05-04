package com._2cha.demo.auth.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

@Data
@JsonSubTypes({
    @JsonSubTypes.Type(JwtAccessTokenPayload.class),
    @JsonSubTypes.Type(JwtRefreshTokenPayload.class)
})
public class JwtTokenPayload {

  private Long sub;
}
