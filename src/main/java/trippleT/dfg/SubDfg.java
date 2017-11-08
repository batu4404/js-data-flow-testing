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

	public void append(SubDfg other) {
		if(other.begin == null) {
			return;
		}
		
		if (begin == null) {
			begin = other.begin;
			end = other.end;
		} else {
			end.next = begin;
			end = other.end;
		}
	}
}
