package trippleT.cfg;

import java.util.ArrayList;

import org.mozilla.javascript.ast.AstNode;

public abstract class CfgNode {
	protected int index;
	protected ArrayList<String> defs;
	protected ArrayList<String> cUses;
	protected ArrayList<String> pUse;
	protected CfgNode next;
	protected CfgNode previous;
	protected AstNode astNode;
	
	public int getIndex() {
		return index;
	}
	
	public CfgNode getNext() {
		return next;
	}
	
	public CfgNode getPrevious() {
		return previous;
	}
	
	public AstNode getAstNode() {
		return astNode;
	}
	
	public void setIndex(int newIndex) {
		index = newIndex;
	}
	
	public void setNext(CfgNode next) {
		this.next = next;
	}
	
	public void setPrevious(CfgNode previous) {
		this.previous = previous;
	}
	
	public void setAstNode(AstNode astNode) {
		this.astNode = astNode;
	}
}
