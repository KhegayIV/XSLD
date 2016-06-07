package ru.nsu.xsld;

public class XsldException extends Exception {
    public XsldException() {
    }

    public XsldException(String message) {
        super(message);
    }

    public XsldException(String message, Throwable cause) {
        super(message, cause);
    }

    public XsldException(Throwable cause) {
        super(cause);
    }

    public XsldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
