package trippleT.cfg;

import org.mozilla.javascript.ast.AstNode;

public class DecisionNode extends CfgNode {
	protected CfgNode falseBranch;
	// trueBranch is next
	
	public AstNode getCondition() {
		return astNode;
	}
}
