package ru.semykin.exception;

public class SupplierException extends RuntimeException {
    public SupplierException(final String message) {
        super(message);
    }
    public SupplierException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public SupplierException(final Throwable cause) {
        super(cause);
    }
}
