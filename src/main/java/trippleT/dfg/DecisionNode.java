package trippleT.dfg;

import org.mozilla.javascript.ast.AstNode;

public class DecisionNode extends DfgNode {
	protected DfgNode falseBranch;
	// trueBranch is next
	protected MergeNode mergeNode;
	
	public DecisionNode() {
		
	}
	
	public DecisionNode(AstNode astNode) {
		super(astNode);
	}
	
	public AstNode getCondition() {
		return astNode;
	}
	
	/**
	 * @return the trueBranch
	 */
	public DfgNode getTrueBranch() {
		return next;
	}

	/**
	 * @param falseBranch the trueBranch to set
	 */
	public void setTrueBranch(DfgNode trueBranch) {
		next = trueBranch;
	}

	/**
	 * @return the falseBranch
	 */
	public DfgNode getFalseBranch() {
		return falseBranch;
	}

	/**
	 * @param falseBranch the falseBranch to set
	 */
	public void setFalseBranch(DfgNode falseBranch) {
		this.falseBranch = falseBranch;
	}

	/**
	 * @return the mergeNode
	 */
	public MergeNode getMergeNode() {
		return mergeNode;
	}

	/**
	 * @param mergeNode the mergeNode to set
	 */
	public void setMergeNode(MergeNode mergeNode) {
		this.mergeNode = mergeNode;
	}
	
	@Override
	public DfgNode getNext() {
		return mergeNode;
	}
	
	public String toString() {
		return astNode.toSource();
	}

}
