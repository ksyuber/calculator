package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class Main {
	
	private static final int PRIORITY_LOW = 1;
	private static final int PRIORITY_HIGH = 2;
	
	private static final char OPERATOR_PLUS = '+';
	private static final char OPERATOR_MINUS = '-';
	private static final char OPERATOR_MULTIPLY = '*';
	private static final char OPERATOR_DIVIDER = '/';
	
	private static final char SYMBOL_ZERO = '0';
	private static final char SYMBOL_NINE = '9';
	private static final char SYMBOL_SPACE = ' ';
	
	public interface Operator {
		public double process(Double arg1, Double arg2);
		public int priority();
	}

	public static class OperatorPlus implements Operator {
		@Override
		public int priority() {
			return PRIORITY_LOW;
		}

		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 + arg2;
		}
	}
	
	public static class OperatorMinus implements Operator {
		@Override
		public int priority() {
			return PRIORITY_LOW;
		}
		
		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 - arg2;
		}
	}
	
	public static class OperatorMultiply implements Operator {
		@Override
		public int priority() {
			return PRIORITY_HIGH;
		}
		
		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 * arg2;
		}
	}
	
	public static class OperatorDivide implements Operator {
		@Override
		public int priority() {
			return PRIORITY_HIGH;
		}
		
		@Override
		public double process(Double arg1, Double arg2) {
			return arg1 / arg2;
		}
	}
	
	public static class OperatorBuilder {
		
		private char operatorSymbol; 
		
		public OperatorBuilder symbol(char operatorSymbol) {
			this.operatorSymbol = operatorSymbol;
			return this;
		}
		
		public Operator build() {
			switch (operatorSymbol) {
			case OPERATOR_PLUS:
				return new OperatorPlus();
			case OPERATOR_MINUS:
				return new OperatorMinus();
			case OPERATOR_MULTIPLY:
				return new OperatorMultiply();
			case OPERATOR_DIVIDER:
				return new OperatorDivide();
			default:
				break;
			}
			
			return null;
		}
	}
	
	private static boolean isNumberSymbol(char symbol) {		
		return symbol >= SYMBOL_ZERO && symbol <= SYMBOL_NINE;		
	}
	
	private static boolean isDecimalSeparator(char symbol) {
		return symbol == DecimalFormatSymbols.getInstance().getDecimalSeparator();
	}
	
	private static boolean isOperatorSymbol(char symbol) {
		return symbol == OPERATOR_PLUS 
				|| symbol == OPERATOR_MINUS 
				|| symbol == OPERATOR_MULTIPLY 
				|| symbol == OPERATOR_DIVIDER;
		
	}
	
	private static boolean isSpace(char symbol) {
		return symbol == ' ';
	}

	// метод, вычисл€ющий значение выражени€
	// lastValue - последнее вычисленное значение, может быть null
	public static double calculate(Double lastValue, String input) throws ParseException {
		Stack<Double> numbers = new Stack<Double>();
		Stack<Operator> operators = new Stack<Operator>();

		if (lastValue != null) {
			numbers.push(lastValue);
		}

		String currentNumber = new String();
		boolean hasSeparator = false;
		boolean hasOperator = false;

		for (int i = 0; i < input.length(); ++i) {
			char currentChar = input.charAt(i);

			boolean isOperator = isOperatorSymbol(currentChar);
			if (i == 0 && !isOperator) {
				numbers.clear();
			}
			
			boolean isNumber = isNumberSymbol(currentChar);
			boolean isDecimalSeparator = isDecimalSeparator(currentChar);
			boolean isSpace = isSpace(currentChar);
			boolean isEnd = i == input.length() - 1;
			boolean isNumberEnd = isOperator || isSpace || isEnd;
			
			if (!isOperator && !isNumber && !isDecimalSeparator && !isSpace) {
				throw new ParseException("Invalid char found", i);
			}

			if (isNumber || isDecimalSeparator) {
				if (isDecimalSeparator) {
					if (hasSeparator) {
						throw new ParseException("Second decimal separator found", i);
					}
					hasSeparator = true;
				}
				currentNumber += currentChar;
			}

			Operator currentOperator = null;
			if (isOperator) {
				if (hasOperator) {
					throw new ParseException("Second operator found", i);
				}
				hasOperator = true;
				currentOperator = new OperatorBuilder()
						.symbol(currentChar)
						.build();
			} else {
				hasOperator = false;
			}

			if (isNumberEnd && !currentNumber.isEmpty()) {
				numbers.push(NumberFormat.getInstance(Locale.getDefault()).parse(currentNumber).doubleValue());
				currentNumber = "";
				hasSeparator = false;
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

	// главна€ функци€, точка входа в программу
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		Double lastValue = null;
		try {
			while (true) {
				System.out.print("> ");
				String command = input.readLine();
	
				if (command == null || command.equals("exit")) {
					break;
				} else {
					lastValue = calculate(lastValue, command);
					System.out.println(lastValue);			
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
