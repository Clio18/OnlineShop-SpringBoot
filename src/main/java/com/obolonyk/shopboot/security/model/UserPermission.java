package com.obolonyk.shopboot.security.model;

public enum UserPermission {
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
