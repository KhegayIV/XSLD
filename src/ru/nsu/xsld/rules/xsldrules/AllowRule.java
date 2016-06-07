package ru.nsu.xsld.rules.xsldrules;

import ru.nsu.xsld.ElementResolver;
import ru.nsu.xsld.PredicateResolver;
import ru.nsu.xsld.paths.UnresolvedPath;
import ru.nsu.xsld.rules.Rule;

/**
 * Created by Илья on 07.06.2016.
 */
public class AllowRule extends Rule {
    public AllowRule(UnresolvedPath path, String condition, String error) {
        super(path, error);
    }

    @Override
    public boolean validate(ElementResolver elementResolver, PredicateResolver predicateResolver) {
        throw new RuntimeException("Not implemented"); // TODO
    }
}
