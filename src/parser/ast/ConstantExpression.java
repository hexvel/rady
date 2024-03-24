package parser.ast;

import lib.Constants;

public class ConstantExpression implements Expression {
    private final String name;

    public ConstantExpression(String name) {
        this.name = name;
    }

    @Override
    public double Eval() {
        if (!Constants.IsExists(name)) throw new RuntimeException("Constant doesn't exists.");
        return Constants.GetByKey(name);
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
}
