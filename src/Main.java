import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.Statement;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String input = "huy = 2";
        final List<Token> tokens = new Lexer(input).tokenize();

        final List<Statement> statements = new Parser(tokens).parse();

        for (Statement statement : statements) {
            System.out.println(statement);
        }
        for (Statement statement : statements) {
            statement.execute();
        }
    }
}
