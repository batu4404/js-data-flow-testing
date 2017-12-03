package trippleT.doSomething;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.UnaryExpression;

import trippleT.utils.rhino.StringGetter;

public class CloneExpression {
	public static void main(String[] args) throws Exception
	{
		String filePath = "test2.js";
		
		CompilerEnvirons env = new CompilerEnvirons();
		env.setRecoverFromErrors(true);
		env.setLanguageVersion(170);
		
		FileReader strReader = new FileReader(filePath);

		IRFactory factory = new IRFactory(env);
		AstRoot rootNode = factory.parse(strReader, null, 0);
		
		AstNode node = (AstNode) rootNode.getFirstChild();
		
		Map<String, AstNode> environment = new HashMap<String, AstNode>();
		Name e = new Name(0, "e");
		environment.put("e", e);
		Name f = new Name(0, "f");
		environment.put("f", f);
		
		while(node != null) {
			if (node instanceof ExpressionStatement) {
				System.out.println(node.getClass());
				ExpressionStatement expSt = (ExpressionStatement) node;
				InfixExpression infix = (InfixExpression) expSt.getExpression();
				AstNode left = infix.getLeft();
				AstNode right = infix.getRight();
				AstNode clone = cloneExpressionAndReplace(right, environment);
				environment.put(left.getString(), clone);
			}
			
			node = (AstNode) node.getNext();
		}
		
		System.out.println(environment);
		for (Map.Entry<String, AstNode> entry : environment.entrySet())
		{
			System.out.println("key: " + entry.getKey());
		    System.out.println("value: " + StringGetter.toSource(entry.getValue()));
		}
		
//		if (firstNode instanceof ExpressionStatement) {
//			System.out.println(firstNode.toSource());
//			ExpressionStatement expSt = (ExpressionStatement) firstNode;
//			AstNode expr = expSt.getExpression();
//			InfixExpression infix = (InfixExpression) expr;
//			AstNode cloneExpr = cloneExpression(expr);
//			String str = StringGetter.toSource(cloneExpr);
//			System.out.println("clone: " + str);
//		}
	}
	
	public static AstNode cloneExpressionAndReplace(AstNode node, Map<String, AstNode> environment) {
		if (node instanceof InfixExpression) {
			InfixExpression infix = (InfixExpression) node;
			AstNode leftOperand = cloneExpressionAndReplace(infix.getLeft(), environment);
			AstNode rightOperand = cloneExpressionAndReplace(infix.getRight(), environment);
			
			InfixExpression cloneExpression = new InfixExpression(leftOperand, rightOperand);
			cloneExpression.setOperator(infix.getOperator());
			return cloneExpression;
		}
		else if (node instanceof UnaryExpression) {
			UnaryExpression unary = (UnaryExpression) node;
			AstNode operand = cloneExpressionAndReplace(unary.getOperand(), environment);
			
			UnaryExpression cloneExpression = new UnaryExpression(unary.getOperator(), -1, operand);
			return cloneExpression;
		}
		else if (node instanceof Name) {
			Name name = (Name) node;
			AstNode var = environment.get(name.getString());
			if (var != null) {
				
				return var;
			}
			return new Name(-1, name.getString());
		}
		else if (node instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenthesize = (ParenthesizedExpression) node;
			AstNode expr = cloneExpressionAndReplace(parenthesize.getExpression(), environment);
			return new ParenthesizedExpression(expr);
		}
		else if (node instanceof NumberLiteral) {
			NumberLiteral numberLiteral = (NumberLiteral) node;
			String value = numberLiteral.getValue();		
			NumberLiteral number = new NumberLiteral(-1, value);
			return number;
		}
		else if (node instanceof KeywordLiteral) {
			KeywordLiteral keywordLiteral = (KeywordLiteral) node;
			KeywordLiteral clone = new KeywordLiteral();
			clone.setType(keywordLiteral.getType());
			return clone;
		}
		
//		System.out.println("class: " + node.getClass());
		
		return null;
	}
	
	public static AstNode cloneExpression(AstNode node) {
		if (node instanceof InfixExpression) {
			InfixExpression infix = (InfixExpression) node;
			AstNode leftOperand = cloneExpression(infix.getLeft());
			AstNode rightOperand = cloneExpression(infix.getRight());
			
			InfixExpression cloneExpression = new InfixExpression(leftOperand, rightOperand);
			cloneExpression.setOperator(infix.getOperator());
			return cloneExpression;
		}
		else if (node instanceof UnaryExpression) {
			UnaryExpression unary = (UnaryExpression) node;
			AstNode operand = cloneExpression(unary.getOperand());
			
			UnaryExpression cloneExpression = new UnaryExpression(unary.getOperator(), -1, operand);
			return cloneExpression;
		}
		else if (node instanceof Name) {
			Name name = (Name) node;
			return new Name(-1, name.getString());
		}
		else if (node instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenthesize = (ParenthesizedExpression) node;
			AstNode expr = cloneExpression(parenthesize.getExpression());
			return new ParenthesizedExpression(expr);
		}
		else if (node instanceof NumberLiteral) {
			NumberLiteral numberLiteral = (NumberLiteral) node;
			String value = numberLiteral.getValue();		
			return new NumberLiteral(-1, value);
		}
		else if (node instanceof KeywordLiteral) {
			KeywordLiteral keywordLiteral = (KeywordLiteral) node;
			KeywordLiteral clone = new KeywordLiteral();
			clone.setType(keywordLiteral.getType());
			return clone;
		}
		
//		System.out.println("class: " + node.getClass());
		
		return null;
	}
}
