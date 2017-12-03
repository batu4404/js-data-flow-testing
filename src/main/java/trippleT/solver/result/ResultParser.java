package trippleT.solver.result;

import java.util.ArrayList;
import java.util.List;

import trippleT.utils.PrefixToInfix;

public class ResultParser {	
	private List<String> listParameter;
	private InputPathResult inputPathResult;
	private List<String> rawSolverResult; // result of running solver
	List<Integer> path;
	
	/**
	 * @return the listParameter
	 */
	public List<String> getListParameter() {
		return listParameter;
	}

	/**
	 * @param listParameter the listParameter to set
	 */
	public void setListParameter(List<String> listParameter) {
		this.listParameter = listParameter;
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

	public InputPathResult generateInputPathResult(List<String> result) {
		
		this.rawSolverResult = result;
		inputPathResult = new InputPathResult();
		inputPathResult.setPath(path);
	
		List<String> listError = new ArrayList<>();
		
		String str;
		String status = null;
		
		int i = 0;
		int n = result.size();
		while (i < n) {
			str = result.get(i);
			
			if (!str.contains("(") && !str.contains(")") && status == null) {
				status = str;
			}
			else if (str.contains("(error")) {
				listError.add(parseError(str));
			}
			else if (str.contains("(model")) {
				// get model string index
				i++;
				int begin = i;
				int end = 0;
				while (i < n) {
					str = result.get(i);
					
					if ( str.equals(")") ) {
						end = i - 1;
						break;
					}
					else {
						i++;
					}
				}
				
				parseModel(begin, end);
			}
			
			i++;
		}
		
		inputPathResult.setErrors(listError);
		inputPathResult.setStatus(status);
		
		return inputPathResult;
	}
	
	private void parseModel(int begin, int end) {
		if (listParameter == null)
			return;
		
		List<DefineFun> paramtersDefineFun = new ArrayList<>();
		int i;
		for (String v: listParameter) {
	    	String varName = v + "_s"; 
	    	
	    	i = begin;
	    	while (i <= end) {
	   
	    		if (rawSolverResult.get(i).indexOf(varName) >= 0) {
	    			String valueStr = "";
	    			i++;
	    			while (i <= end && !rawSolverResult.get(i).contains("define-fun")) {
	    				valueStr += rawSolverResult.get(i);
	    				i++;
	    			}
	    			
	    			String value = getValue(valueStr);
	    			paramtersDefineFun.add(new DefineFun(v, value));
	    			break;
	    		}
	
	    		i++;
	    	}
	    }
		
		
		inputPathResult.setParameters(paramtersDefineFun);
	}
	
	private String getValue(String valueStr) {
		valueStr = valueStr.replace('(', ' ')
							.replace(')', ' ')
							.trim();
		
		String value = PrefixToInfix.prefixToInfix(valueStr);
		
		return value;
	}
	public static String parseError(String error) {
		int begin = error.indexOf("\"") + 1;
		int end = error.lastIndexOf("\"");
		
		return error.substring(begin, end);
	}
	
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<>();
		list.add("sat");
		list.add("(model ");
		list.add("  (define-fun b_s () Real");
		list.add("    0.0)");
		list.add("  (define-fun a_s () Real");
		list.add("    0.0)");
		list.add(")");
		list.add("(:assert-lower 1");
		list.add(" :assert-upper 1");
		list.add(" :final-checks 1");
		list.add(" :memory       2.90");
		list.add(" :pivots       1");
		list.add(" :time         0.00");
		list.add(" :total-time   0.01)");
		
		List<String> listParameter = new ArrayList<>();
		listParameter.add("a");
		listParameter.add("b");
		
		ResultParser report = new ResultParser();
		report.setListParameter(listParameter);
		InputPathResult verificationReport= report.generateInputPathResult(list);
		verificationReport.print();
	}
}
