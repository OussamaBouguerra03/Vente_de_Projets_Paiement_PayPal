package com.bouguerra.dev.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, CUSTOMER, DEVELOPPER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}