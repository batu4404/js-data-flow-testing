package trippleT.dfg;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;

public class DfgBuilder {
	private int index = 0;
	
	public Dfg buildDfg(FunctionNode function) {
		AstNode body = function.getBody();
		return null;
	}
	
	public void buildBodySubDfg(AstNode body) {
		if (body instanceof Block) {
			buildBlockSubDfg((Block) body);
		}
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
		}
		
		return subDfg;
	}
	
	public SubDfg buildIfSubDfg(IfStatement ifStatement) {
		SubDfg subDfg = new SubDfg();
		
		ifStatement.getCondition();
		
		return subDfg;
	}
	
	public SubDfg buildSubDfg(AstNode node) {
		if (node instanceof ExpressionStatement) {
			return buildExpressionSubDfg((ExpressionStatement) node);
		} else if (node instanceof Block) {
			return buildBlockSubDfg((Block) node);
		}
		
		return null;
	}
	
	public SubDfg buildExpressionSubDfg(ExpressionStatement expressionStatement) {
		AstNode expression = expressionStatement.getExpression();
		Statement statement = new Statement(expression);
		
		SubDfg subDfg = new SubDfg(statement, statement);
		
		return subDfg;
	}
}
