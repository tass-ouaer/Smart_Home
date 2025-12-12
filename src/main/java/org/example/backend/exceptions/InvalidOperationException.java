package org.example.backend.exceptions;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException() {
        super("Invalid operation performed!");
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
