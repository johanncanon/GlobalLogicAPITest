package com.johanncanon.globallogic.user_management_service.exception.model;

import java.time.Instant;

public class ErrorDetail {

    private Instant timeStamp;
    private int codigo;
    private String detail;

    public ErrorDetail(int codigo, String detail) {
        this.timeStamp = Instant.now();
        this.codigo = codigo;
        this.detail = detail;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDetail() {
        return detail;
    }

}
