package trippleT.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableInitializer;

import trippleT.cfg.CfgNode;
import trippleT.cfg.DecisionNode;
import trippleT.doSomething.CloneExpression;
import trippleT.utils.rhino.StringGetter;

public class PathExecution {
    public void executePath(List<Integer> path, List<String> params, Map<Integer, CfgNode> nodeMap) {
        Map<String, AstNode> environment = new HashMap<String, AstNode>();
        List<AstNode> pathConstraints = new ArrayList<AstNode>();
        
        addParamsToEnvironment(params, environment);
        
        CfgNode cfgNode;
        for (Integer nodeIndex : path) {
            cfgNode = nodeMap.get(Math.abs(nodeIndex));
            if (cfgNode instanceof DecisionNode) {
                DecisionNode decision = (DecisionNode) cfgNode;
                AstNode condition = null;
                if (nodeIndex < 0) {
                    condition = negativeCondition(decision.getCondition(), environment);
                } else {
                    condition = CloneExpression.cloneExpressionAndReplace(decision.getCondition(), environment);
                }
                
                pathConstraints.add(condition);
            } else {
                putVariable(cfgNode.getAstNode(), environment);
            }
        }
        
        for (AstNode constraint : pathConstraints) {
            System.out.println("constraint: " + StringGetter.toSource(constraint));
        }
    }
    
    public AstNode negativeCondition(AstNode node, Map<String, AstNode> environment) {
        AstNode clone = CloneExpression.cloneExpressionAndReplace(node, environment);
        UnaryExpression unary = new UnaryExpression();
        unary.setOperand(clone);
        unary.setOperator(Token.NOT);
        
        return unary;
    }
    
    public void putVariable(AstNode node, Map<String, AstNode> environment) {
        if (node instanceof Assignment) {
            Assignment assignment = (Assignment) node;
            AstNode right = CloneExpression.cloneExpressionAndReplace(assignment.getRight(), environment);
            AstNode left = assignment.getLeft();
            String varName = left.getString();
            environment.put(varName, right);
        }
        else if (node instanceof VariableInitializer) {
            VariableInitializer varInit = (VariableInitializer) node;
            AstNode target = varInit.getTarget();
            AstNode init = varInit.getInitializer();
            if (init != null) {
                AstNode clone = CloneExpression.cloneExpressionAndReplace(init, environment);
                environment.put(target.getString(), clone);
            }
        }
    }
    
    public void addParamsToEnvironment(List<String> params, Map<String, AstNode> environment) {
        for (String param : params) {
            Name var = new Name();
            var.setString(param + "_S");
            environment.put(param, var);
        }
        
        for (Map.Entry<String, AstNode> entry : environment.entrySet())
        {
            System.out.println("key: " + entry.getKey());
                System.out.println("value: " + StringGetter.toSource(entry.getValue()));
        }
    }
}
