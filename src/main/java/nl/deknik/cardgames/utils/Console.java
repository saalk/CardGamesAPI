package nl.deknik.cardgames.utils;

import java.util.Scanner;

public class Console {

	String question = null;
	String[] options = null;

	public Console(String question, String[] options) {
		this.question = question;
		this.options = options;
	}

	public int getAnswerFromConsole(String inputQuestion, String[] inputOptions) throws Exception {

		if (inputQuestion == null || inputOptions == null) {
			throw new Exception("No question or options supplied");
		}

		String displayOptions;
		displayOptions = appendOptionsToString(inputOptions);
		System.out.print(inputQuestion + displayOptions);

		boolean validChoice = false;
		Scanner s = new Scanner(System.in);
		int choice = 0;

		do {
			validChoice = true;
			

			try {
				choice = safeParseInt(s.nextLine());

			} catch (Exception ec) {
				// s.reset()
				choice = safeParseInt(s.next());
			}

			if (choice == 0) {
				validChoice = false;
				System.out.print("That is not a valid number, choose again" + displayOptions);
			} else if (choice < 0 || choice > inputOptions.length) {
				validChoice = false;
				System.out.print("That is not a valid option, choose again" + displayOptions);
			}
		} while (!validChoice);
		s.reset();
		return choice;

	}

	public int autoAnswerOnConsole(String inputQuestion, String[] inputOptions, Integer overrideAnswer)

			throws Exception {

		if (inputQuestion == null || inputOptions == null || overrideAnswer < 1) {
			throw new Exception("No question, options or answer supplied");
		}

		String displayOptions;
		displayOptions = appendOptionsToString(inputOptions);

		System.out.println(inputQuestion + displayOptions + overrideAnswer);
		return overrideAnswer;
	}

	public static int safeParseInt(String str) {
		int result;
		try {
			result = Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			result = 0;
		}
		return result;
	}

	private String appendOptionsToString(String[] inputOptions) {

		// make a nice series of options to display from the inputOptions list
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" [");

		int optionsTotal = inputOptions.length;
		int i = 0;

		for (String o : inputOptions) {
			i += 1;
			stringBuilder.append(i);
			stringBuilder.append("=");
			stringBuilder.append(o);
			if (optionsTotal > 1 && i != optionsTotal) {
				stringBuilder.append(", ");
			}
		}

		stringBuilder.append("]: ");
		String displayOptions = stringBuilder.toString();

		return displayOptions;
	}

}
