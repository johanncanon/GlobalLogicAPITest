package com.johanncanon.globallogic.user_management_service.exception.model;

import java.util.List;

public class ErrorResponse {

    private List<ErrorDetail> error;

    public ErrorResponse(List<ErrorDetail> error) {
        this.error = error;
    }

    public List<ErrorDetail> getError() {
        return error;
    }

}
