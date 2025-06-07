package com.johanncanon.globallogic.user_management_service.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "El campo Name es requerido")
    private String username;

    @NotBlank(message = "El campo Password es mandatorio")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
