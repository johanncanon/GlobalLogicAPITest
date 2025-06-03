package com.johanncanon.globallogic.user_management_service.dto;

public class UserResponse {

    public Long id;
    public String created;
    public String lastLogin;
    public String token;
    public Boolean isActive;
    
    public UserResponse() {}

    public UserResponse(Long id, String created, String lastLogin, String token, Boolean isActive) {
        this.id = id;
        this.created = created;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getToken() {
        return token;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    

    
}
