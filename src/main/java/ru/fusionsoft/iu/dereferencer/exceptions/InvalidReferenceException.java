package ru.fusionsoft.iu.dereferencer.exceptions;

public class InvalidReferenceException extends Exception {
    public InvalidReferenceException() {
        super();
    }

    public InvalidReferenceException(String message) {
        super(message);
    }

    public InvalidReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReferenceException(Throwable cause) {
        super(cause);
    }

    protected InvalidReferenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
