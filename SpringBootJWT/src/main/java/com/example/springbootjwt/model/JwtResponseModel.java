package com.example.springbootjwt.model;

import java.io.Serializable;

public class JwtResponseModel implements Serializable {
    private final String token;
    public JwtResponseModel(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
}
