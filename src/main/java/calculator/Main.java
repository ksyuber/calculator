package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.*;

public class Main {
	
	public interface Operator {
		public double process(Double arg1, Double arg2);
		public int priority();
	}

	public static class OperatorPlus implements Operator {
		@Override
		public int priority() {
			return 1;
		}

		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 + arg2;
		}
	}
	
	public static class OperatorMinus implements Operator {
		@Override
		public int priority() {
			return 1;
		}
		
		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 - arg2;
		}
	}
	
	public static class OperatorMultiply implements Operator {
		@Override
		public int priority() {
			return 2;
		}
		
		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 * arg2;
		}
	}
	
	public static class OperatorDivide implements Operator {
		@Override
		public int priority() {
			return 2;
		}
		
		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 / arg2;
		}
	}
	
	public static class OperatorBuilder {
		
		private char m_operatorSymbol; 
		
		public OperatorBuilder symbol(char operatorSymbol) {
			m_operatorSymbol = operatorSymbol;
			return this;
		}
		
		public Operator build() {
			switch (m_operatorSymbol) {
			case '+':
				return new OperatorPlus();
			case '-':
				return new OperatorMinus();
			case '*':
				return new OperatorMultiply();
			case '/':
				return new OperatorDivide();
			default:
				break;
			}
			
			return null;
		}
	}
	
//	// класс оператора, определяет его приоритет и производит вычисления над
//	// аргументами
//	public static class Operator {
//		const public char OPERATOR_PLUS = '+';
//		// сам оператор
//		public char operator;
//		// приоритет оператора. У * и / приоритет выше чем у + и -
//		public int priority;
//
//		// конструктор оператора, принимает символ оператора и определяет его
//		// приоритет
//		public Operator(char op) {
//			operator = op;
//			if (op == OPERATOR_PLUS || op == '-') {
//				priority = 1;
//			} else if (op == '*' || op == '/') {
//				priority = 2;
//			}
//		}
//
//		// вычисляет результат применения оператора над двумя аргументами
//		public double process(Double arg1, Double arg2) {
//			if (operator == '+') {
//				return arg1 + arg2;
//			} else if (operator == '-') {
//				return arg1 - arg2;
//			} else if (operator == '*') {
//				return arg1 * arg2;
//			} else if (operator == '/') {
//				return arg1 / arg2;
//			} else {
//				return 0;
//			}
//		}
//	}
	
	private static boolean isNumberSymbol(char symbol) {
		return symbol >= '0' && symbol <= '9';
	}
	
	private static boolean isOperatorSymbol(char symbol) {
		return symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/';
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

			boolean isOperator = isOperatorSymbol(currentChar);
			boolean isNumber = isNumberSymbol(currentChar);
			boolean isEnd = i == input.length() - 1;
			boolean isNumberEnd = isOperator || currentChar == ' ' || isEnd;

			if (isNumber) {
				currentNumber += currentChar;
			}

			Operator currentOperator = null;
			if (isOperatorSymbol(currentChar)) {
				currentOperator = new OperatorBuilder()
						.symbol(currentChar)
						.build();
			}

			if (isNumberEnd && !currentNumber.isEmpty()) {
				numbers.push(Double.parseDouble(currentNumber));
				currentNumber = "";
			}

			if (isOperator || isEnd) {
				while (operators.size() > 0 && numbers.size() > 1
						&& (isEnd || isOperator && operators.peek().priority() >= currentOperator.priority())) {
					Double arg2 = numbers.pop();
					Double arg1 = numbers.pop();
					numbers.push(operators.pop().process(arg1, arg2));
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
