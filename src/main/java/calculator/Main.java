package calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (true) {
				System.out.print("> ");
				String command = input.readLine();
				
				if (command.equals("exit")) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}
}
