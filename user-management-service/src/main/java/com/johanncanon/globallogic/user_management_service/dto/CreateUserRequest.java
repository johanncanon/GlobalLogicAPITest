package com.johanncanon.globallogic.user_management_service.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateUserRequest {

    private String name;

    @Size(min = 8, max = 12, message = "La contraseña debe tener entre 8 y 12 caracteres")
    @Pattern(
        regexp = "^(?=(.*[A-Z]){1})(?=(.*\\d){2})(?!.*[A-Z].*[A-Z])(?!.*\\d.*\\d.*\\d)[a-zA-Z0-9]{8,12}$",
        message = "La contraseña debe tener: 1 mayúscula, exactamente 2 números, solo caracteres alfanuméricos"
    )
    private String password;

    @Email( message = "El email no es válido" )
    @NotBlank( message = "El email es requerido" )
    @Size( max = 120, message = "El email no puede tener más de 120 caracteres" )
    @Pattern( regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El email no es válido" )
    private String email;
    
    private List<PhoneRequest> phones;
    
    public CreateUserRequest() {
    }

    public CreateUserRequest(String name, String password, String email, List<PhoneRequest> phones) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PhoneRequest> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneRequest> phones) {
        this.phones = phones;
    }

    
    

}
