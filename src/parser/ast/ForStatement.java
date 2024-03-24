package parser.ast;

public class ForStatement implements Statement {

	private final Statement initialize;
	private final Expression termintation;
	private final Statement increment;
	private final Statement statement;

	public ForStatement(Statement initialize, Expression termintation, Statement increment, Statement statement) {
		this.initialize = initialize;
		this.termintation = termintation;
		this.increment = increment;
		this.statement = statement;
	}

	@Override
	public void execute() {
		// while (condition) statement

		for (initialize.execute(); termintation.eval().asDouble() != 0; increment.execute()) {
			try {
				statement.execute();
			} catch (BreakStatement bs) {
				break;
			}
		}
	}

	@Override
	public String toString() {
		return "for " + initialize + "; " + termintation + "; " + increment + " " + statement;
	}
}
