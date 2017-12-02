package trippleT.testpath;

import java.util.List;
import java.util.Map;

import org.mozilla.javascript.ast.AstNode;

import trippleT.cfg.CfgNode;

public class PathExecution {
	public void executePath(List<Integer> path, Map<Integer, CfgNode> nodeMap) {
		Map<String, AstNode> environment;
		
		CfgNode cfgNode;
		for (Integer nodeIndex : path) {
			cfgNode = nodeMap.get(nodeIndex);
		}
	}
}
