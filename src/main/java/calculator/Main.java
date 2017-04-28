package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.*;

public class Main {

	// класс оператора, определяет его приоритет и производит вычисления над
	// аргументами
	public static class Operator {
		// сам оператор
		public char operator;
		// приоритет оператора. У * и / приоритет выше чем у + и -
		public int priority;

		// конструктор оператора, принимает символ оператора и определяет его
		// приоритет
		public Operator(char op) {
			operator = op;
			if (op == '+' || op == '-') {
				priority = 1;
			} else if (op == '*' || op == '/') {
				priority = 2;
			}
		}

		// вычисляет результат применения оператора над двумя аргументами
		public double process(Double arg1, Double arg2) {
			if (operator == '+') {
				return arg1 + arg2;
			} else if (operator == '-') {
				return arg1 - arg2;
			} else if (operator == '*') {
				return arg1 * arg2;
			} else if (operator == '/') {
				return arg1 / arg2;
			} else {
				return 0;
			}
		}
	}

	// метод, вычисляющий значение выражения
	// lastValue - последнее вычисленное значение, может быть null
	public static double calculate(Double lastValue, String input) {
		Stack<Double> numbers = new Stack<Double>();
		Stack<Operator> operators = new Stack<Operator>();

		if (lastValue != null) {
			numbers.push(lastValue);
		}

		String currentNumber = new String();

		for (int i = 0; i < input.length(); ++i) {
			char currentChar = input.charAt(i);

			boolean isNumberPart = currentChar >= '0' && currentChar <= '9';
			boolean isOperator = currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/';
			boolean isEnd = i == input.length() - 1;
			boolean isNumberEnd = isOperator || currentChar == ' ' || isEnd;

			if (isNumberPart) {
				currentNumber += currentChar;
			}

			Operator currentOperator = null;
			if (isOperator) {
				currentOperator = new Operator(currentChar);
			}

			if (isNumberEnd && !currentNumber.isEmpty()) {
				numbers.push(Double.parseDouble(currentNumber));
				currentNumber = "";
			}

			if (isOperator || isEnd) {
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

	// главная функция, точка входа в программу
	public static void main(String[] args) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		Double lastValue = null;
		while (true) {
			System.out.print("> ");
			String command = input.readLine();

			if (command.equals("exit")) {
				break;
			} else {
				lastValue = calculate(lastValue, command);
				System.out.println(lastValue);
			}
		}
	}
}
