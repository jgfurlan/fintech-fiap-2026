package br.com.fiap.fintech.exception;

public class FintechNotFoundException extends RuntimeException {
    public FintechNotFoundException(String message) {
        super(message);
    }
}
