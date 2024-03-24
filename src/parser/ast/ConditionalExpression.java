package parser.ast;

import lib.NumberValue;
import lib.StringValue;
import lib.Value;

public class ConditionalExpression implements Expression {
    public static enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),

        EQUAL("=="),
        NOT_EQUAL("!="),

        LT("<"),
        GT(">"),
        LTEQ("<="),
        GTEQ(">="),

        AND("&&"),
        OR("||");

        private final String operator;

        Operator(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }
    }

    private final Expression expr1, expr2;
    private final Operator operation;


    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        final Value firstValue = expr1.eval();
        final Value secondValue = expr2.eval();

        double firstNumber;
        double secondNumber;

        if (firstValue instanceof StringValue) {
            firstNumber = firstValue.asString().compareTo(secondValue.asString());
            secondNumber = 0;
        } else {
            firstNumber = firstValue.asDouble();
            secondNumber = secondValue.asDouble();
        }

        boolean result;
        switch (operation) {
            case LT -> {
                result = firstNumber < secondNumber;
            }
            case GT -> {
                result = firstNumber > secondNumber;
            }
            case LTEQ -> {
                result = firstNumber <= secondNumber;
            }
            case GTEQ -> {
                result = firstNumber >= secondNumber;
            }
            case EQUAL -> {
                result = firstNumber == secondNumber;
            }
            case NOT_EQUAL -> {
                result = firstNumber != secondNumber;
            }
            case AND -> {
                result = firstNumber != 0 && secondNumber != 0;
            }
            case OR -> {
                result = firstNumber != 0 || secondNumber != 0;
            }
            default -> throw new RuntimeException("Unknown operator");
        }

        return new NumberValue(result);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", expr1, operation.getOperator(), expr2);
    }
}
