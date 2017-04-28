package calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.*;

public class Main {

	public static class Operator {
		public char operator;
		public int priority;

		public Operator(char op) throws InvalidParameterException {
			operator = op;
			if (op == '+' || op == '-') {
				priority = 1;
			} else if (op == '*' || op == '/') {
				priority = 2;
			} else {
				throw new InvalidParameterException("Invalid operator");
			}
		}

		public double process(Double arg1, Double arg2) {
			if (operator == '+') {
				return arg1 + arg2;
			} else if (operator == '-') {
				return arg1 - arg2;
			} else if (operator == '*') {
				return arg1 * arg2;
			} else {
				return arg1 / arg2;
			}
		}
	}

	public static double calculate(String input) {
		Stack<Double> numbers = new Stack<Double>();
		Stack<Operator> operators = new Stack<Operator>();

		String currentNumber = new String();

		for (int i = 0; i < input.length(); ++i) {
			char currentChar = input.charAt(i);

			if (currentChar >= '0' && currentChar <= '9') {
				currentNumber += currentChar;
			}

			boolean isOperator = currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/';
			boolean isEnd = i == input.length() - 1;
			boolean isNumberEnd = currentChar == ' ' || isEnd;
			if ((isOperator || isNumberEnd) && !currentNumber.isEmpty()) {
				numbers.push(Double.parseDouble(currentNumber));
				currentNumber = "";
			}

			if (isOperator || isEnd) {
				Operator currentOperator = null;
				if (isOperator) {
					currentOperator = new Operator(currentChar);
				}

				while (operators.size() > 0 && numbers.size() > 1
						&& (isEnd || isOperator && operators.peek().priority >= currentOperator.priority)) {
					Double arg2 = numbers.pop();
					Double arg1 = numbers.pop();
					Operator lastOperator = operators.pop();
					numbers.push(lastOperator.process(arg1, arg2));
				}

				if (isOperator) {
					operators.push(currentOperator);
				}
			}
		}

		return numbers.peek();
	}

	public static void main(String[] args) {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.print("> ");
				String command = input.readLine();

				if (command.equals("exit")) {
					break;
				} else {
					System.out.println(calculate(command));
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e.toString());
			}
		}
	}
}
