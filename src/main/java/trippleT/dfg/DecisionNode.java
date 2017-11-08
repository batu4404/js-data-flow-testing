package trippleT.dfg;

import org.mozilla.javascript.ast.AstNode;

public class DecisionNode extends DfgNode {
	protected DfgNode falseBranch;
	// trueBranch is next
	
	public AstNode getCondition() {
		return astNode;
	}
}
