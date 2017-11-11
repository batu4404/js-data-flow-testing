package trippleT.dfg;

public class Dfg implements IDfg {
	private BeginNode begin;
	private EndNode end;
	
	public Dfg(SubDfg subDfg) {
		if (subDfg.getBegin() != null) {
			begin = new BeginNode();
			begin.next = subDfg.getBegin();
			end = new EndNode();
			subDfg.getEnd().setNext(end);
		}
	}
	
	/**
	 * @return the begin
	 */
	public BeginNode getBegin() {
		return begin;
	}
	/**
	 * @param begin the begin to set
	 */
	public void setBegin(BeginNode begin) {
		this.begin = begin;
	}
	/**
	 * @return the end
	 */
	public EndNode getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(EndNode end) {
		this.end = end;
	}
}
