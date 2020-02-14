package query;

import java.util.ArrayList;

import codeGenerator.LambdaFunctionModifier;
import scanner.SelectiveQueryScanner;

public class LambdaFunction {
	private String mainFunction;
	private ArrayList<String> variables;
	private ArrayList<String> tokens;
	private LambdaFunctionModifier modifier;

	public LambdaFunction(String mainFunction) {
		this.mainFunction = mainFunction;
		this.tokens = parseTokens(mainFunction);
		this.variables = extractVariables(this.tokens);
		this.modifier = new LambdaFunctionModifier(this.tokens, this.variables);
	}

	private ArrayList<String> extractVariables(ArrayList<String> tokens) {
		ArrayList<String> variables = new ArrayList<String>();
		for (String token : tokens) {
			if (token.trim().equals("->")) {
				break;
			}
			variables.add(token.replace("\\", ""));
		}
		return variables;
	}

	private ArrayList<String> parseTokens(String lambda) {
		SelectiveQueryScanner scanner = new SelectiveQueryScanner();
		ArrayList<String> tokens = scanner.scanLambdaFunction(lambda);
		return tokens;
	}

	public ArrayList<String> getVariables() {
		return this.variables;
	}

	public String getMainFunction() {
		return this.mainFunction;
	}

	public void setMainFunction(String mainFunction) {
		this.mainFunction = mainFunction;
	}

	public ArrayList<String> getTokens() {
		return this.tokens;
	}

	public void setTokens(ArrayList<String> tokens) {
		this.tokens = tokens;
	}

	public String toString() {
		String result = this.mainFunction + "\n" + "    Tokens: " + this.tokens.toString();
		return result;
	}

	public String flattenLambdaFunction() {
		String result = "";
		for (String token : this.tokens) {
			if (!token.equals("")) {
				result += token + " ";
			}
		}
		return result;
	}
	
	public void modifyConsInLambdaFunction(String newConsFunction, Integer amountOfParametersInDomainDataModelConsFunction) {
		this.modifier.modifyConsInLambdaFunction(newConsFunction, amountOfParametersInDomainDataModelConsFunction);
	}
	
	public void substituteNil(String targetInitialCollection) {
		this.modifier.substituteNil(targetInitialCollection);
	}

}
