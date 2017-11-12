package trippleT.cfg;

import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;

public class CfgBuilder {
	private int index = 0;
	
	public Cfg buildCfg(FunctionNode function) {
		AstNode body = function.getBody();
		index = 0;
		SubCfg subCfg = buildSubCfg(body);
		Cfg cfg = new Cfg(subCfg);
		return cfg;
	}
	
	public SubCfg buildSubCfg(AstNode node) {
		if (node instanceof VariableDeclaration) {
			return buildVariableDeclarationSubCfg((VariableDeclaration) node);
		} else if (node instanceof VariableInitializer) {
			return buildVariableInitializerSubCfg((VariableInitializer) node);
		} else if (node instanceof ExpressionStatement) {
			return buildExpressionSubCfg((ExpressionStatement) node);
		} else if (node instanceof Block) {
			return buildBlockSubCfg((Block) node);
		} else if (node instanceof IfStatement) {
			return buildIfSubCfg((IfStatement) node);
		} else if (node instanceof ForLoop) {
			return buildForLoopSubCfg((ForLoop) node);
		} else if (node instanceof Scope) {
			return buildScopeSubCfg((Scope) node);
		} else if (node instanceof ReturnStatement) {
			return buildReturnSubCfg((ReturnStatement) node);
		} else if (node instanceof UnaryExpression) {
			return buildUnarySubCfg((UnaryExpression) node);
		}
		
		return new SubCfg();
	}
	
	public SubCfg buildUnarySubCfg(UnaryExpression unary) {
		int operator = unary.getOperator();
		NumberLiteral delta = new NumberLiteral();
		delta.setValue("1");
		
		AstNode left = unary.getOperand();
		InfixExpression right = new InfixExpression(left, delta);
		if (operator == Token.INC) {
			right.setOperator(Token.ADD);
		} else if (operator == Token.DEC) {
			right.setOperator(Token.SUB);
		}
		Assignment assignment = new Assignment(left, right);
		Statement statement = new Statement(assignment);
		
		return new SubCfg(statement, statement);
	}

	public SubCfg buildReturnSubCfg(ReturnStatement returnStatement) {
		Statement statement = new Statement(returnStatement);
		return new SubCfg(statement, statement);
	}

	public void buildBodySubCfg(AstNode body) {
		if (body instanceof Block) {
			buildBlockSubCfg((Block) body);
		}
	}
	
	public SubCfg buildVariableDeclarationSubCfg(VariableDeclaration varDecl) {
		List<VariableInitializer> variables = varDecl.getVariables();
		SubCfg subCfg = new SubCfg();
		SubCfg temp = null;
		for (VariableInitializer var: variables) {
			temp = buildVariableInitializerSubCfg(var);
			subCfg.append(temp);
		}
		
		return subCfg;
	}
	
	public SubCfg buildVariableInitializerSubCfg(VariableInitializer varInit) {
		if (varInit.getInitializer() != null) {
			Statement statement = new Statement(varInit);
			return new SubCfg(statement, statement);
		}
		
		return new SubCfg();
	}
	
	public SubCfg buildBlockSubCfg(Block block) {
		SubCfg subCfg = new SubCfg();
		AstNode next = (AstNode) block.getFirstChild();
		SubCfg temp = null;
		while (next != null) {
			temp = buildSubCfg(next);
			if (temp != null) {
				subCfg.append(temp);
			}
			next = (AstNode) next.getNext();
		}
		
		return subCfg;
	}
	
	public SubCfg buildScopeSubCfg(Scope scope) {
		SubCfg subCfg = new SubCfg();
		AstNode next = (AstNode) scope.getFirstChild();
		SubCfg temp = null;
		while (next != null) {
			temp = buildSubCfg(next);
			if (temp != null) {
				subCfg.append(temp);
			}
			next = (AstNode) next.getNext();
		}
		
		return subCfg;
	}
	
	public SubCfg buildIfSubCfg(IfStatement ifStatement) {
		AstNode condition = ifStatement.getCondition();
		SubCfg trueBranch = buildSubCfg(ifStatement.getThenPart());
		SubCfg falseBranch = buildSubCfg(ifStatement.getElsePart());
		MergeNode mergeNode = new MergeNode();
		
		trueBranch.appendNode(mergeNode);
		falseBranch.appendNode(mergeNode);
		
		SubCfg subCfg = buildDecisionSubCfg(condition, trueBranch, falseBranch, mergeNode);
		
		return subCfg;
	}
	
	public SubCfg buildForLoopSubCfg(ForLoop forLoop) {
		SubCfg initializers = buildSubCfg(forLoop.getInitializer());
		SubCfg body = buildSubCfg(forLoop.getBody());
		SubCfg increment = buildSubCfg(forLoop.getIncrement());
		SubCfg trueBranch = body.append(increment);
		SubCfg falseBranch = new SubCfg();
		
		MergeNode mergeNode = new MergeNode();
		trueBranch.appendNode(mergeNode);
		falseBranch.appendNode(mergeNode);
		
		AstNode condition = forLoop.getCondition();
		SubCfg decisionCfg = buildDecisionSubCfg(condition, trueBranch, falseBranch, mergeNode);
		
		SubCfg subCfg = initializers.append(decisionCfg);

		return subCfg;
	}
	
	public SubCfg buildExpressionSubCfg(ExpressionStatement expressionStatement) {
		AstNode expression = expressionStatement.getExpression();
		Statement statement = new Statement(expression);
		SubCfg subCfg = new SubCfg(statement, statement);
		
		return subCfg;
	}
	
	public SubCfg buildDecisionSubCfg(AstNode condition, SubCfg trueBranch,
										SubCfg falseBranch, MergeNode mergeNode) {
		
		DecisionNode decisionNode = new DecisionNode(condition);
		decisionNode.setTrueBranch(trueBranch.getBegin());
		decisionNode.setFalseBranch(falseBranch.getBegin());
		decisionNode.setMergeNode(mergeNode);
		
		return new SubCfg(decisionNode, mergeNode);
	}
}
