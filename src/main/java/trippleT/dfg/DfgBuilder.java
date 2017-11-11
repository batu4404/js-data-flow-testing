package trippleT.dfg;

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

public class DfgBuilder {
	private int index = 0;
	
	public Dfg buildDfg(FunctionNode function) {
		AstNode body = function.getBody();
		SubDfg subDfg = buildSubDfg(body);
		subDfg.print();
		Dfg dfg = new Dfg(subDfg);
		return dfg;
	}
	
	public SubDfg buildSubDfg(AstNode node) {
		if (node instanceof VariableDeclaration) {
			return buildVariableDeclarationSubDfg((VariableDeclaration) node);
		} else if (node instanceof VariableInitializer) {
			return buildVariableInitializerSubDfg((VariableInitializer) node);
		} else if (node instanceof ExpressionStatement) {
			return buildExpressionSubDfg((ExpressionStatement) node);
		} else if (node instanceof Block) {
			return buildBlockSubDfg((Block) node);
		} else if (node instanceof IfStatement) {
			return buildIfSubDfg((IfStatement) node);
		} else if (node instanceof ForLoop) {
			return buildForLoopSubDfg((ForLoop) node);
		} else if (node instanceof Scope) {
			return buildScopeSubDfg((Scope) node);
		} else if (node instanceof ReturnStatement) {
			return buildReturnSubDfg((ReturnStatement) node);
		} else if (node instanceof UnaryExpression) {
			return buildUnarySubDfg((UnaryExpression) node);
		}
		
		return new SubDfg();
	}
	
	public SubDfg buildUnarySubDfg(UnaryExpression unary) {
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
		
		return new SubDfg(statement, statement);
	}

	public SubDfg buildReturnSubDfg(ReturnStatement returnStatement) {
		Statement statement = new Statement(returnStatement);
		return new SubDfg(statement, statement);
	}

	public void buildBodySubDfg(AstNode body) {
		if (body instanceof Block) {
			buildBlockSubDfg((Block) body);
		}
	}
	
	public SubDfg buildVariableDeclarationSubDfg(VariableDeclaration varDecl) {
		List<VariableInitializer> variables = varDecl.getVariables();
		SubDfg subDfg = new SubDfg();
		SubDfg temp = null;
		for (VariableInitializer var: variables) {
			temp = buildVariableInitializerSubDfg(var);
			subDfg.append(temp);
		}
		
		return subDfg;
	}
	
	public SubDfg buildVariableInitializerSubDfg(VariableInitializer varInit) {
		if (varInit.getInitializer() != null) {
			Statement statement = new Statement(varInit);
			return new SubDfg(statement, statement);
		}
		
		return new SubDfg();
	}
	
	public SubDfg buildBlockSubDfg(Block block) {
		SubDfg subDfg = new SubDfg();
		AstNode next = (AstNode) block.getFirstChild();
		SubDfg temp = null;
		while (next != null) {
			temp = buildSubDfg(next);
			if (temp != null) {
				subDfg.append(temp);
			}
			next = (AstNode) next.getNext();
		}
		
		return subDfg;
	}
	
	public SubDfg buildScopeSubDfg(Scope scope) {
		SubDfg subDfg = new SubDfg();
		AstNode next = (AstNode) scope.getFirstChild();
		SubDfg temp = null;
		while (next != null) {
			temp = buildSubDfg(next);
			if (temp != null) {
				subDfg.append(temp);
			}
			next = (AstNode) next.getNext();
		}
		
		return subDfg;
	}
	
	public SubDfg buildIfSubDfg(IfStatement ifStatement) {
		AstNode condition = ifStatement.getCondition();
		SubDfg trueBranch = buildSubDfg(ifStatement.getThenPart());
		SubDfg falseBranch = buildSubDfg(ifStatement.getElsePart());
		MergeNode mergeNode = new MergeNode();
		
		trueBranch.appendNode(mergeNode);
		falseBranch.appendNode(mergeNode);
		
		SubDfg subDfg = buildDecisionSubDfg(condition, trueBranch, falseBranch, mergeNode);
		
		return subDfg;
	}
	
	public SubDfg buildForLoopSubDfg(ForLoop forLoop) {
		SubDfg initializers = buildSubDfg(forLoop.getInitializer());
		SubDfg body = buildSubDfg(forLoop.getBody());
		SubDfg increment = buildSubDfg(forLoop.getIncrement());
		SubDfg trueBranch = body.append(increment);
		SubDfg falseBranch = new SubDfg();
		
		MergeNode mergeNode = new MergeNode();
		trueBranch.appendNode(mergeNode);
		falseBranch.appendNode(mergeNode);
		
		AstNode condition = forLoop.getCondition();
		SubDfg decisionDfg = buildDecisionSubDfg(condition, trueBranch, falseBranch, mergeNode);
		
		SubDfg subDfg = initializers.append(decisionDfg);

		return subDfg;
	}
	
	public SubDfg buildExpressionSubDfg(ExpressionStatement expressionStatement) {
		AstNode expression = expressionStatement.getExpression();
		Statement statement = new Statement(expression);
		SubDfg subDfg = new SubDfg(statement, statement);
		
		return subDfg;
	}
	
	public SubDfg buildDecisionSubDfg(AstNode condition, SubDfg trueBranch,
										SubDfg falseBranch, MergeNode mergeNode) {
		
		DecisionNode decisionNode = new DecisionNode(condition);
		decisionNode.setTrueBranch(trueBranch.getBegin());
		decisionNode.setFalseBranch(falseBranch.getBegin());
		decisionNode.setMergeNode(mergeNode);
		
		return new SubDfg(decisionNode, mergeNode);
	}
}
