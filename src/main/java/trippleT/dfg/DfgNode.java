package trippleT.dfg;

import java.util.ArrayList;

import org.mozilla.javascript.ast.AstNode;

public abstract class DfgNode {
	protected int index = -1;
	protected ArrayList<String> defs;
	protected ArrayList<String> cUses;
	protected ArrayList<String> pUse;
	protected DfgNode next;
	protected DfgNode previous;
	protected AstNode astNode;
	
	public DfgNode() {
		
	}
	
	public DfgNode(AstNode astNode) {
		this.astNode = astNode;
	}
	
	public DfgNode(AstNode astNode, DfgNode next, DfgNode previous) {
		this.astNode = astNode;
		this.next = next;
		this.previous = previous;
	}
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the defs
	 */
	public ArrayList<String> getDefs() {
		return defs;
	}
	/**
	 * @param defs the defs to set
	 */
	public void setDefs(ArrayList<String> defs) {
		this.defs = defs;
	}
	/**
	 * @return the cUses
	 */
	public ArrayList<String> getcUses() {
		return cUses;
	}
	/**
	 * @param cUses the cUses to set
	 */
	public void setcUses(ArrayList<String> cUses) {
		this.cUses = cUses;
	}
	/**
	 * @return the pUse
	 */
	public ArrayList<String> getpUse() {
		return pUse;
	}
	/**
	 * @param pUse the pUse to set
	 */
	public void setpUse(ArrayList<String> pUse) {
		this.pUse = pUse;
	}
	/**
	 * @return the next
	 */
	public DfgNode getNext() {
		return next;
	}
	/**
	 * @param next the next to set
	 */
	public void setNext(DfgNode next) {
		this.next = next;
	}
	/**
	 * @return the previous
	 */
	public DfgNode getPrevious() {
		return previous;
	}
	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(DfgNode previous) {
		this.previous = previous;
	}
	/**
	 * @return the astNode
	 */
	public AstNode getAstNode() {
		return astNode;
	}
	/**
	 * @param astNode the astNode to set
	 */
	public void setAstNode(AstNode astNode) {
		this.astNode = astNode;
	}
}
