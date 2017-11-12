package trippleT.cfg;

public class Cfg extends AbstractCfg {
	public Cfg(SubCfg subDfg) {
		if (subDfg.getBegin() != null) {
			begin = new BeginNode();
			begin.next = subDfg.getBegin();
			end = new EndNode();
			subDfg.getEnd().setNext(end);
		}
	}
}
