package trippleT.cfg;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.VariableInitializer;

import trippleT.cfg.Cfg;
import trippleT.cfg.CfgBuilder;
import trippleT.doSomething.CloneExpression;


public class BuildDfgTest {

	public static void main(String[] args) throws Exception
	{
		String filePath = "test.js";
		
		BuildDfgTest demo = new BuildDfgTest();
		demo.parseJS(filePath);
	}
	
	public void parseJS (String filePath) throws Exception
	{
		CompilerEnvirons env = new CompilerEnvirons();
		env.setRecoverFromErrors(true);
		env.setLanguageVersion(170);
		
		FileReader strReader = new FileReader(filePath);

		IRFactory factory = new IRFactory(env);
		AstRoot rootNode = factory.parse(strReader, null, 0);
		
		Node firstNode = rootNode.getFirstChild();
		
		
		if (firstNode instanceof FunctionNode) {
			FunctionNode function = (FunctionNode) firstNode;
			CfgBuilder builder = new CfgBuilder();
			Cfg cfg = builder.buildCfg(function);
//			cfg.print();
			
			List<String> params = cfg.getParams();
		
			
			List<List<Integer>> allPaths = cfg.getAllPossiblePaths();
			for (List<Integer> currentPath: allPaths) {
				Map<String, AstNode> environment = new HashMap<String, AstNode>();
				for (String param: params) {
					Name name = new Name(0, param+"_s");
					environment.put(param, name);
				}
				Map<Integer, CfgNode> nodeMap = cfg.getNodeMap();
				List<CfgNode> cfgNodeList = new ArrayList<CfgNode>();
				List<String> constraints = new ArrayList<String>();
				for(Integer keyOfNode: currentPath) {
					CfgNode currentNode = (keyOfNode > 0)?nodeMap.get(keyOfNode):nodeMap.get(-keyOfNode);
					if (currentNode.getAstNode() instanceof Assignment) {
						
//						
//						Assignment expSt = (Assigntment) currentNode.getAstNode();
						InfixExpression infix = (InfixExpression) currentNode.getAstNode();
						AstNode left = infix.getLeft();
						AstNode right = infix.getRight();
//						System.out.println(environment.get("a").getString());
//						System.out.println(right.toSource() + " " + right.getClass());
						AstNode clone = CloneExpression.cloneExpressionAndReplace(right, environment);
						environment.put(left.getString(), clone);
//						if (clone instanceof NumberLiteral) {
//							NumberLiteral number = (NumberLiteral) clone;
//							System.out.println(left.getString() + " " + number.getValue());
//						} else if (clone instanceof InfixExpression) {
//							System.out.println(left.getString() + " " + clone.toSource());
//						}
//						else {
//							System.out.println("clone " + clone.getString());
//						}
						
					} else if (currentNode.getAstNode() instanceof VariableInitializer) {
						VariableInitializer vi = (VariableInitializer) currentNode.getAstNode();
						AstNode right = vi.getInitializer();
						AstNode left = vi.getTarget();
						AstNode clone = CloneExpression.cloneExpressionAndReplace(right, environment);
						environment.put(left.getString(), clone);
//						if (clone instanceof NumberLiteral) {
//							NumberLiteral number = (NumberLiteral) clone;
//							System.out.println(left.getString() + " " + number.getValue());
//						} else if (clone instanceof InfixExpression) {
//							System.out.println(left.getString() + " " + clone.toSource());
//						}
//						else {
//							System.out.println("clone " + clone.getString());
//						}
					} else if (currentNode instanceof DecisionNode) {
						InfixExpression infix = (InfixExpression) currentNode.getAstNode();
						AstNode left = infix.getLeft();
						AstNode right = infix.getRight();
						AstNode leftCondition = CloneExpression.cloneExpressionAndReplace(left, environment);
						AstNode rightCondition = CloneExpression.cloneExpressionAndReplace(right, environment);
						String constraint;
						if (keyOfNode > 0) {
							constraint = leftCondition.toSource() + infix.operatorToString(infix.getOperator()) + rightCondition.toSource();
						} else {
							constraint = "!(" + leftCondition.toSource() + infix.operatorToString(infix.getOperator()) + rightCondition.toSource() + ")";
						}
						constraints.add(constraint);
					}
				}
				System.out.println(constraints);
			}
			System.out.println(allPaths);
		}
	}	
}
