import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.Expression;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String input = "E";
        final List<Token> tokens = new Lexer(input).Tokenize();

        final List<Expression> expressions = new Parser(tokens).Parse();
        for (Expression expr : expressions) {
            System.out.println(expr + " = " + expr.Eval());
        }
    }
}
