package trippleT.solver.result;

import java.util.List;

public class InputPathResult {
	public static final int POSSIBLE_PATH = 1;
	public static final int IMPOSSIBLE_PATH = 0;
	public static final String POSSIBLE_PATH_STR = "Possible path";
	public static final String IMPOSSIBLE_PATH_STR = "Impossible path";

	private int status;
	private List<DefineFun> parameters;
	private List<String> errors;
	private List<Integer> path;
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String SMTStatus) {
		if ("sat".equals(SMTStatus)) {
			status = POSSIBLE_PATH;
		}
		else if ("unsat".equals(SMTStatus)) {
			status = IMPOSSIBLE_PATH;
		}
		else {
			status = IMPOSSIBLE_PATH;
		}
	}
	
	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}
	
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * @return the parameters
	 */
	public List<DefineFun> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<DefineFun> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * @return the path
	 */
	public List<Integer> getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(List<Integer> path) {
		this.path = path;
	}
	
	public String getInput() {
		String result = "";
		if (parameters != null) {
			for (DefineFun param: parameters) {
				result += param.getExpression() + " ";
			}
		}
		
		return result;
	}

	public void print() {
		if (status == POSSIBLE_PATH) {
			if (parameters != null) {
				System.out.println("Input of path: " + path);
				for (DefineFun param: parameters) {
					System.out.println(param);
				}
			}
		} else {
			System.out.println("Impossible path: " + path);
		}
	}
}
