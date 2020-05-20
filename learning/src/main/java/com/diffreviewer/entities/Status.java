package com.diffreviewer.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Status implements GrantedAuthority {
    MERGED,
    NOT_MERGED,
    CLOSED,
    Completed,
    WIP;

    @Override
    public String getAuthority() {
        return name();
    }
}
