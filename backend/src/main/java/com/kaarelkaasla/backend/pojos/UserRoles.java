package com.kaarelkaasla.backend.pojos;

import java.util.List;

public class UserRoles {
    private String username;
    private List<Integer> roles;

    public UserRoles(String username, List<Integer> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
}
