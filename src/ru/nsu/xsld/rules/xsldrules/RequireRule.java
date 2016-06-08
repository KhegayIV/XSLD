package ru.nsu.xsld.rules.xsldrules;

import ru.nsu.xsld.parsing.ElementResolver;
import ru.nsu.xsld.PredicateResolver;
import ru.nsu.xsld.parsing.UnresolvedPath;
import ru.nsu.xsld.rules.Rule;

/**
 * Created by Илья on 07.06.2016.
 */
public class RequireRule extends Rule {
    public RequireRule(UnresolvedPath path, String condition, String error) {
        super(path, error);
    }

    @Override
    public boolean validate(ElementResolver elementResolver, PredicateResolver predicateResolver) {
        throw new RuntimeException("Not implemented"); // TODO
    }
}
