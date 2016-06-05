package ru.nsu.xsld;

/**
 * Interface that catches all errors, found during validation
 */
public interface XsldErrorListener {
    void onError(String fieldLabel, String errorMessage);
}
