package com.johanncanon.globallogic.user_management_service.exception.handler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.johanncanon.globallogic.user_management_service.exception.custom.BadRequestException;
import com.johanncanon.globallogic.user_management_service.exception.custom.InvalidCredentialsException;
import com.johanncanon.globallogic.user_management_service.exception.custom.ResourceNotFoundException;
import com.johanncanon.globallogic.user_management_service.exception.custom.UnauthorizedException;
import com.johanncanon.globallogic.user_management_service.exception.custom.UserAlreadyExistsException;
import com.johanncanon.globallogic.user_management_service.exception.model.ErrorDetail;
import com.johanncanon.globallogic.user_management_service.exception.model.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException exception) {
        var errorDetail = new ErrorDetail(400, exception.getMessage());
        var errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception) {
        var errorDetail = new ErrorDetail(404, exception.getMessage());
        var errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExist(UserAlreadyExistsException exception) {
        var errorDetail = new ErrorDetail(409, exception.getMessage());
        var errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredential(InvalidCredentialsException exception) {
        var errorDetail = new ErrorDetail(401, exception.getMessage());
        var errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException exception) {
        var errorDetail = new ErrorDetail(401, exception.getMessage());
        var errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        var errorDetail = new ErrorDetail(500, "Error interno del servidor, por favor intente nuevamente.");
        var errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetail(400, error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }

}
