package trippleT.dfg;

public class Dfg implements IDfg {
	private BeginNode begin;
	private EndNode end;
	
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
