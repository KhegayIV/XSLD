package ru.nsu.xsld.interpreters;


import java.util.Map;
import java.util.Optional;

/**
 * Interface that is used to interpret predicates defined in XSLD schema in &lt predicate ... &gt tags
 */
public interface PredicateInterpreter {
    /**
     * Handles predicate and returns it's value from arguments
     *
     * @param name      predicate name as defined in schema
     * @param arguments predicate argument names as defined in schema mapped to values from XML
     * @return true if predicate is handled by this interpreter and returns true. False otherwise.
     */

    Optional<Boolean> interpret(String name, Map<String, String> arguments);
}
