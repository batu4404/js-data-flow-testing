package trippleT.dfg;

import org.mozilla.javascript.ast.AstNode;

public class Statement extends DfgNode {
	public Statement() {
		super();
	}
	
	public Statement(AstNode expression) {
		super(expression);
	}
	
	public AstNode getExpression() {
		return astNode;
	}
}
