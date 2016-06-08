package ru.nsu.xsld.rules;

/**
 * Created by Илья on 06.06.2016.
 */
class RuleNode {

    private Rule rule;

    public void block() {
        throw new RuntimeException("Not implemented"); // TODO
    }

    public void reset() {
        throw new RuntimeException("Not implemented"); // TODO
    }

    public Rule getRule() {
        return rule;
    }
}
