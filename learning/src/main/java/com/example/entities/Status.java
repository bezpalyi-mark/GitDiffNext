package com.example.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Status implements GrantedAuthority {
    MERGED,
    NOT_MERGED,
    CLOSED;

    @Override
    public String getAuthority() {
        return name();
    }
}
