package br.com.fiap.fintech.exception;

import br.com.fiap.fintech.dto.ErrorDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FintechGlobalExceptionHandler {

    @ExceptionHandler(FintechAuthException.class)
    public ResponseEntity<ErrorDetailResponse> handleAuthException(FintechAuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDetailResponse(ex.getMessage()));
    }

    @ExceptionHandler(FintechNotFoundException.class)
    public ResponseEntity<ErrorDetailResponse> handleNotFoundException(FintechNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDetailResponse(ex.getMessage()));
    }

    @ExceptionHandler(FintechBadRequestException.class)
    public ResponseEntity<ErrorDetailResponse> handleBadRequestException(FintechBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDetailResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDetailResponse(ex.getMessage() != null ? ex.getMessage() : "Internal server error"));
    }
}
