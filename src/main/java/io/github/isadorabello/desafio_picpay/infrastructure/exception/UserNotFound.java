package io.github.isadorabello.desafio_picpay.infrastructure.exception;

public class UserNotFound extends RuntimeException {
  public UserNotFound(String message) {
    super(message);
  }
}
