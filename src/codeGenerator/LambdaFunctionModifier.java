package codeGenerator;

import java.util.ArrayList;

import com.sun.tools.javac.util.Pair;

public class LambdaFunctionModifier {

	private ArrayList<String> variables;
	private ArrayList<String> tokens;

	public LambdaFunctionModifier(ArrayList<String> tokens, ArrayList<String> variables) {
		this.tokens = tokens;
		this.variables = variables;
	}

	public void modifyConsInLambdaFunction(String newConsFunction,
			int amountOfParametersInDomainDataModelConsFunction) {
		// System.out.println("New cons function: " + newConsFunction + " and amount of
		// parameters: " + amountOfParametersInDomainDataModelConsFunction);
		switch (newConsFunction) {
		case ":":
			switch (amountOfParametersInDomainDataModelConsFunction) {
			case 1:
				modifyConsInLambdaFunctionOneParameter(newConsFunction);
				break;
			case 2:
				modifyConsInLambdaFunctionTwoParameters(newConsFunction);
				break;
			default:
				System.out.println("Amount of parameters in domain data model's cons function is "
						+ amountOfParametersInDomainDataModelConsFunction + " which is invalid amount.");
				break;
			}
			break;
		default:
			modifyLambdaFunction("cons", newConsFunction);
			break;
		}
	}

	public void modifyConsInLambdaFunctionOneParameter(String newConsFunction) {
		for (int i = 0; i < this.tokens.size(); i++) {
			boolean visitedIf = false;

			if (this.tokens.get(i).trim().equals("cons")
					&& this.tokens.get(i + 1).trim().equals(this.variables.get(0))) {
				this.tokens.set(i, "[" + this.variables.get(0) + "]");
				this.tokens.set(i + 1, "");
				visitedIf = true;
			} else if (i == this.tokens.size() - 1 && visitedIf) {
				System.out.println("Error! The cons function in the lambda function has wrong parameters. Should be "
						+ this.variables.get(0) + ".");
			} else if (this.tokens.get(i).trim().equals("cons") && this.tokens.get(i + 1).trim().equals("(")) {
				modifyConsFunctionFollowedByOneOrTwoParametersClosedInParantheses(i + 1);
			}
		}
	}

	public void modifyConsInLambdaFunctionTwoParameters(String newConsFunction) {
		for (int i = 0; i < this.tokens.size(); i++) {
			boolean visitedIf = false;
			String token = this.tokens.get(i).trim();
			if (token.equals("cons") && this.tokens.get(i + 1).trim().equals(this.variables.get(0))
					&& this.tokens.get(i + 2).trim().equals(this.variables.get(1))) {
				this.tokens.set(i, this.variables.get(0));
				this.tokens.set(i + 1, newConsFunction);
				this.tokens.set(i + 2, this.variables.get(1));
				visitedIf = true;
			} else if (token.equals("cons") && this.tokens.get(i + 1).trim().equals("(")) {
				modifyConsFunctionFollowedByOneOrTwoParametersClosedInParantheses(i);
				visitedIf = true;
			} else if (token.equals("cons")) {
				this.tokens.set(i, this.tokens.get(i + 1));
				this.tokens.set(i + 1, newConsFunction);
				visitedIf = true;
			} else if (i == this.tokens.size() - 1 && visitedIf) {
				System.out.println("Error! The cons function in the lambda function has wrong parameters. Should be "
						+ this.variables.get(0) + " and " + this.variables.get(1));
			}
		}
	}

	private void modifyConsFunctionFollowedByOneOrTwoParametersClosedInParantheses(int i) {
		Pair<String, Integer> element = findUntilParanthesisClose(i + 1);
		if (element == null) {
			System.out.println("Wrong amount of paranthesis after cons function!");
		}
		this.tokens.set(i, element.fst);
		if (this.tokens.get(element.snd).trim().equals(this.variables.get(1))) {
			this.tokens.set(element.snd, ": " + this.variables.get(1));
		} else if (this.tokens.get(element.snd).trim().equals("(")) {
			Pair<String, Integer> element2 = findUntilParanthesisClose(element.snd);
			if (element2 == null) {
				System.out.println("Wrong amount of paranthesis after cons function!");
			}
			this.tokens.set(element.snd, ":" + element2.fst);
		} else {
			this.tokens.set(element.snd, ": " + this.tokens.get(element.snd));
		}
	}

	private Pair<String, Integer> findUntilParanthesisClose(int i) {
		String result = "(";
		this.tokens.set(i, "");
		Integer paranthesesCount = 0;
		for (int j = i + 1; j < this.tokens.size(); j++) {
			String token = this.tokens.get(j).trim();
			if (token.equals(")") && paranthesesCount == 0) {
				this.tokens.set(j, "");
				return new Pair<String, Integer>(result + ")", j + 1);
			} else if (token.equals(")") && paranthesesCount > 0) {
				paranthesesCount--;
			} else if (token.equals("(")) {
				paranthesesCount++;
			}
			result += token + " ";
			this.tokens.set(j, "");
		}
		return null;
	}

	public void modifyLambdaFunction(String token, String newToken) {
		for (int i = 0; i < this.tokens.size(); i++) {
			if (this.tokens.get(i).trim().equals(token)) {
				this.tokens.set(i, newToken);
			}
		}
	}

	public void substituteNil(String newEmptyCollection) {
		for (int i = 0; i < this.tokens.size(); i++) {
			String token = this.tokens.get(i);
			if (token.contains("nil")) {
				this.tokens.set(i, token.replaceAll("nil", newEmptyCollection));
			}
		}
	}

}
