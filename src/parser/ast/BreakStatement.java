package parser.ast;

public class BreakStatement extends RuntimeException implements Statement {

	@Override
	public void execute() {
		throw this;
	}

	@Override
	public String toString() {
		return "break";
	}
}
