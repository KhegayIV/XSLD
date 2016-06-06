package ru.nsu.xsld;

import ru.nsu.xsld.interpreters.ErrorInterpreter;
import ru.nsu.xsld.interpreters.PredicateInterpreter;
import ru.nsu.xsld.interpreters.ErrorListener;
import ru.nsu.xsld.rules.RuleSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Илья on 06.06.2016.
 */
class RuleChecker {

    private List<ErrorInterpreter> errorInterpreters = new ArrayList<>();
    private List<PredicateInterpreter> predicateInterpreters = new ArrayList<>();

    public boolean checkRuleSet(RuleSet ruleSet, ElementResolver resolver, ErrorListener listener){
        throw new RuntimeException("Not implemented"); // TODO
    }

    /**
     * Add error interpreter to schema
     */
    void addErrorInterpreter(ErrorInterpreter interpreter){
        errorInterpreters.add(interpreter);
    }

    /**
     * Add predicate interpreter to schema
     */
    void addPredicateInterpreter(PredicateInterpreter interpreter){
        predicateInterpreters.add(interpreter);
    }
}
