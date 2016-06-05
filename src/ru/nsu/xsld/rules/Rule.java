package ru.nsu.xsld.rules;

import ru.nsu.xsld.ElementResolver;
import ru.nsu.xsld.PredicateResolver;

/**
 * Created by Илья on 06.06.2016.
 */
public  abstract class Rule {
    private String address;
    private String error;

    protected Rule(String address, String error) {
        this.address = address;
        this.error = error;
    }

    public String getAddress() {
        return address;
    }

    public String getError() {
        return error;
    }

    public abstract void validate(ElementResolver elementResolver, PredicateResolver predicateResolver);
}
