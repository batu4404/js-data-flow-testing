package trippleT.doSomething;

import java.io.FileReader;
import java.util.List;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
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
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;


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
		
		
		
//		IRFactory factory = new IRFactory(env);
//		AstRoot rootNode = factory.parse(strReader, null, 0);
//		env.get
		env.setLanguageVersion(170);
		System.out.println("env: " + env.getLanguageVersion());
		Parser parser = new Parser(env);
		
		AstRoot rootNode = parser.parse(strReader, null, 0);
//		
		List functionCount = rootNode.getFunctions();
		System.out.println("function count: " + functionCount.size());
		
		Node firstNode = rootNode.getFirstChild();
		System.out.println("firstnode: " + firstNode);
		
		visit((AstNode) firstNode);
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
		if (node instanceof FunctionNode) {
			visitFunction((FunctionNode) node);
		} else if (node instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement) node;
			AstNode thenBranch = ifStatement.getThenPart();
			AstNode elseBranch = ifStatement.getElsePart();
			AstNode condition = ((IfStatement) node).getCondition();
			System.out.println("condition: " + condition.toSource());
			visitCondition(condition);
			System.out.println("then: " + thenBranch.getClass());
			System.out.println("else: " + elseBranch);
		} else if (node instanceof Block) {
			Block block = (Block) node;
			if (block.hasChildren()) {
				AstNode firstNode = (AstNode) block.getFirstChild();
				visit(firstNode);
			}
		} else if (node instanceof Assignment) {
			System.out.println("assignment: " + node.toSource());
			System.out.println("assignment: " + node.getClass());
		} else if (node instanceof ExpressionStatement) {
//			ExpressionStatement statement = (ExpressionStatement) node;
			System.out.println("expression statement: " + node.toSource());
			AstNode expression = ((ExpressionStatement) node).getExpression();
			visitExpression(expression);
		} else if (node instanceof VariableDeclaration) {
			VariableDeclaration varDecl = (VariableDeclaration) node;
			List<VariableInitializer> variables = varDecl.getVariables();
			for (VariableInitializer var: variables) {
				visit(var);
			}
		} else if (node instanceof VariableInitializer) {
			System.out.println("init: " + node.toSource());
			VariableInitializer varInit = (VariableInitializer) node;
			AstNode init = varInit.getInitializer();
			visit(init);
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
			visitInfixExpression((InfixExpression) expression);
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
	
	public static void visitInfixExpression(InfixExpression expression) {
		if (expression instanceof Assignment) {
			System.out.println("assignment" + expression.toSource());
		} 
		
		InfixExpression infix = (InfixExpression) expression;
		AstNode rightHand = infix.getRight();
		AstNode leftHand = infix.getLeft();
		System.out.println("infix expression: " + expression.toSource());
		System.out.println("infix expression: " + expression);
		visitExpression(leftHand);
		visitExpression(rightHand);
	}
}
