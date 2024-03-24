import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import parser.Lexer;
import parser.Parser;
import parser.Token;
import parser.ast.Statement;

public class Main {
    public static void main(String[] args) throws IOException {
        final String input = Files.readString(Paths.get("main.rady"));
        final List<Token> tokens = new Lexer(input).tokenize();

        final Statement program = new Parser(tokens).parse();
        program.execute();
    }
}
