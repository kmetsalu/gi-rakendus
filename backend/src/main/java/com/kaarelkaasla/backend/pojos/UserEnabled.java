package com.kaarelkaasla.backend.pojos;

public class UserEnabled {
    private String username;
    private Boolean isEnabled;

    public UserEnabled(String username, Boolean isEnabled) {
        this.username = username;
        this.isEnabled = isEnabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
