package trippleT.dfg;

import org.mozilla.javascript.ast.AstNode;

import trippleT.utils.rhino.StringGetter;

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
	
	public String toString() {
		return StringGetter.toSource(astNode);
	}
}
