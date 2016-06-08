package ru.nsu.xsld.rules;

import ru.nsu.xsld.parsing.ElementResolver;
import ru.nsu.xsld.PredicateResolver;
import ru.nsu.xsld.parsing.UnresolvedPath;

/**
 * Created by Илья on 06.06.2016.
 */
public  abstract class Rule {
    private UnresolvedPath path;
    private String error;

    protected Rule(UnresolvedPath path, String error) {
        this.path = path;
        this.error = error;
    }

    public UnresolvedPath getPath() {
        return path;
    }

    public String getError() {
        return error;
    }

    public abstract boolean validate(ElementResolver elementResolver, PredicateResolver predicateResolver);
}
