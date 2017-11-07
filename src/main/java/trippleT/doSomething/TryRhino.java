package trippleT.doSomething;

import java.io.FileReader;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.UnaryExpression;


public class TryRhino {

	public static void main(String[] args) throws Exception
	{
		String filePath = "test.js";
		
		TryRhino demo = new TryRhino();
		demo.parseJS(filePath);
	}
	
	public void parseJS (String filePath) throws Exception
	{
		CompilerEnvirons env = new CompilerEnvirons();
		env.setRecoverFromErrors(true);
		
		FileReader strReader = new FileReader(filePath);

		IRFactory factory = new IRFactory(env);
		AstRoot rootNode = factory.parse(strReader, null, 0);
		
		List functionCount = rootNode.getFunctions();
		System.out.println("function count: " + functionCount.size());
		
		Node firstNode = rootNode.getFirstChild();
		
		if (firstNode instanceof FunctionNode) {
			visitFunction((FunctionNode) firstNode);
		}
	}	
	
	public static void visitFunction(FunctionNode node) {
		AstNode body = node.getBody();
		System.out.println("body: " + body);
		if (body instanceof Block) {
			Block block = (Block) body;
//			System.out.println("block: " + block.toSource());
//			AstNode firstNode = (AstNode) block.getFirstChild();
//			System.out.println("fist child: " + firstNode.toSource());
			visit(block);
		}
	}
	
	public static void visit(AstNode node) {
		if (node instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement) node;
			AstNode condition = ((IfStatement) node).getCondition();
			System.out.println("condition: " + condition.toSource());
			visitCondition(condition);
		} else if (node instanceof Block) {
			Block block = (Block) node;
			AstNode firstNode = (AstNode) block.getFirstChild();
			visit(firstNode);
		} else if (node instanceof Assignment) {
			System.out.println("assignment: " + node.toSource());
			System.out.println("assignment: " + node.getClass());
		}	else if (node instanceof ExpressionStatement) {
//			ExpressionStatement statement = (ExpressionStatement) node;
			System.out.println("expression statement: " + node.toSource());
			AstNode expression = ((ExpressionStatement) node).getExpression();
			visitExpression(expression);
		} else {
			System.out.println("unkonw node: " + node.toSource() + "  " + node.getClass());
//			System.out.println("node: " + node.toSource() + "  " + node.getClass());
		}
		
		AstNode next = (AstNode) node.getNext();
		if (next != null) {
			visit(next);
		}
	}
	
	public static void visitCondition(AstNode condition) {
		System.out.println("condition type: " + condition.getType());
		System.out.println("condition class: " + condition.getClass());
		visitExpression(condition);
	}
	
	public static void visitExpression(AstNode expression) {
		if (expression instanceof InfixExpression) {
			InfixExpression infix = (InfixExpression) expression;
			AstNode rightHand = infix.getRight();
			AstNode leftHand = infix.getLeft();
			System.out.println("infix expression: " + expression.toSource());
			visitExpression(leftHand);
			visitExpression(rightHand);
		} else if (expression instanceof UnaryExpression) {
			System.out.println("unary expression: " + expression.toSource());
		} else if (expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenthesizedExp = (ParenthesizedExpression) expression;
			AstNode exp = parenthesizedExp.getExpression();
			visitExpression(exp);
		} else {
			System.out.println("unknown expression: " + expression.toSource());
			System.out.println("unknown expression: " + expression.getClass());
		}
	}
}
