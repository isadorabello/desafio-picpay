package io.github.isadorabello.desafio_picpay.infrastructure.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
