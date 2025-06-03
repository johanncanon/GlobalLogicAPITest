package com.johanncanon.globallogic.user_management_service.dto;

public class JwtResponse {

    private String token;
    private String type = "Bearer";

   public JwtResponse() {}

   public JwtResponse( String token, String type ) {
    this.token = token;
    this.type = type;
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
   
}
