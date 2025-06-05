package com.johanncanon.globallogic.user_management_service.dto;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private UserResponse user;

    public JwtResponse() {
    }

    public JwtResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public JwtResponse(String token, String type, UserResponse user) {
        this.token = token;
        this.type = type;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

}
