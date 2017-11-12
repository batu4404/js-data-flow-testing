package trippleT.cfg;

import java.io.FileReader;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionNode;

import trippleT.cfg.Cfg;
import trippleT.cfg.CfgBuilder;


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
			cfg.print();
		}
	}	
}
