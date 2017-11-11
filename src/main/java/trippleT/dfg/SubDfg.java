package trippleT.dfg;

public class SubDfg implements IDfg {
	private DfgNode begin;
	private DfgNode end;
	
	public SubDfg() {
		// todo
	}
	
	public SubDfg(DfgNode begin, DfgNode end) {
		this.begin = begin;
		this.end = end;
	}
	
	/**
	 * @return the begin
	 */
	public DfgNode getBegin() {
		return begin;
	}
	/**
	 * @param begin the begin to set
	 */
	public void setBegin(DfgNode begin) {
		this.begin = begin;
	}
	/**
	 * @return the end
	 */
	public DfgNode getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(DfgNode end) {
		this.end = end;
	}

	public SubDfg append(SubDfg other) {
		if(other.begin != null) {
			if (begin == null) {
				begin = other.begin;
				end = other.end;
			} else {
				end.next = other.begin;
				end = other.end;
			}
		}
		
		return this;
	}
	
	public SubDfg appendNode(DfgNode node) {
		if(node != null) {
			if (begin == null) {
				end = begin = node;
			} else {
				end.next = node;
				end = node;
			}
		}
		
		return this;
	}
	
	public void print() {
		if (begin == null) {
			return;
		}
		
		print(begin, null);
	}
	
	public void print(DfgNode node, DfgNode end) {
		if (node == null || node == end) {
			return;
		}
		
		if (node instanceof DecisionNode) {
			DecisionNode decision = (DecisionNode) node;
			System.out.println("Decision Node: " + node);
			MergeNode mergeNode = decision.getMergeNode();
			print(decision.getTrueBranch(), mergeNode);
			print(decision.getFalseBranch(), mergeNode);
			print(mergeNode, end);
		} else if (node instanceof Statement) {
			System.out.println("statement: " + node);
			print(node.getNext(), end);
		} else {
			print(node.getNext(), end);
		}
	}
}
