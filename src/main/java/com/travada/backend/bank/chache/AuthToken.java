package com.travada.backend.bank.chache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class AuthToken implements Serializable {
    private String tokenId;
    private String token;
}
