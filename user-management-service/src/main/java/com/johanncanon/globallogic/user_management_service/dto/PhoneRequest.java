package com.johanncanon.globallogic.user_management_service.dto;

public class PhoneRequest {

    private String number;
    private String citiCode;
    private String countryCode;
    
    public PhoneRequest() {}

    public PhoneRequest(String number, String citiCode, String countryCode) {
        this.number = number;
        this.citiCode = citiCode;
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCitiCode() {
        return citiCode;
    }

    public void setCitiCode(String citiCode) {
        this.citiCode = citiCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    

    
}
