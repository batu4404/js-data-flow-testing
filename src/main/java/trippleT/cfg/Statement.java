package trippleT.cfg;

import org.mozilla.javascript.ast.AstNode;

import trippleT.utils.rhino.StringGetter;

public class Statement extends CfgNode {
	public Statement() {
		super();
	}
	
	public Statement(AstNode expression) {
		super(expression);
	}
	
	public Statement(AstNode expression, int index) {
		super(expression, index);
	}
	
	public AstNode getExpression() {
		return astNode;
	}
	
	public String toString() {
		return StringGetter.toSource(astNode);
	}
}
