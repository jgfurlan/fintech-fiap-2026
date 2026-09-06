package br.com.fiap.fintech.exception;

public class FintechBadRequestException extends RuntimeException {
    public FintechBadRequestException(String message) {
        super(message);
    }
}
