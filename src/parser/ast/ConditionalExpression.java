package parser.ast;

import lib.NumberValue;
import lib.StringValue;
import lib.Value;

public class ConditionalExpression implements Expression {
    private final Expression expr1, expr2;
    private final char operation;


    public ConditionalExpression(char operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        final Value firstValue = expr1.eval();
        final Value secondValue = expr2.eval();

        if (firstValue instanceof StringValue) {
            final String firstString = firstValue.asString();
            final String secondString = secondValue.asString();

            return switch (operation) {
                case '<' -> new NumberValue(firstString.compareTo(secondString) < 0);
                case '>' -> new NumberValue(firstString.compareTo(secondString) > 0);
                default -> new NumberValue(firstString.equals(secondString));
            };
        }

        final double firstNumber = firstValue.asDouble();
        final double secondSecond = secondValue.asDouble();

        return switch (operation) {
            case '<' -> new NumberValue(firstNumber < secondSecond);
            case '>' -> new NumberValue(firstNumber > secondSecond);
            case '=' -> new NumberValue(firstNumber == secondSecond);
            default -> new NumberValue(firstNumber + secondSecond);
        };
    }

    @Override
    public String toString() {
        return String.format("%s %c %s", expr1, operation, expr2);
    }
}
