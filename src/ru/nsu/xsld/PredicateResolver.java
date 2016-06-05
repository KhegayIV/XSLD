package ru.nsu.xsld;

/**
 * Created by Илья on 06.06.2016.
 */
public interface PredicateResolver {
    boolean resolve(String predicate, ElementResolver resolver);
}
