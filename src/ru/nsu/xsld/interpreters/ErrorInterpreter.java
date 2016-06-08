package ru.nsu.xsld.interpreters;

import com.sun.istack.internal.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Interface that is used to interpret errors defined in XSLD schema in &lt error ... &gt tags
 */
public interface ErrorInterpreter {

    /**
     * Handles error and returns string representation
     *
     * @param name      error name as defined in schema
     * @param arguments error argument names as defined in schema mapped to values from XML
     * @return string representing error message or null, if such error isn't interpreted by this interpreter
     */
    @Nullable
    Optional<String> interpret(String name, Map<String, String> arguments);
}
