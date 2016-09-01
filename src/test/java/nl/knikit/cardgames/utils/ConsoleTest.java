package nl.knikit.cardgames.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConsoleTest {

	String[] testOneOption = null;
	String[] testFourOptions = null;
	Integer testInputAnswer = null;
	int testNoOverrideOption;
	int testOneOverrideOption;

	int result = 0; 

	Console testConsoleWithError = new Console(null, null);
	Console testConsoleWithOneOption = new Console(null, null);
	Console testConsoleWithFourOptions = new Console(null, null);
		
	@Before
	public void setUp() throws Exception {

		testInputAnswer = 0;
		testNoOverrideOption = 0;
		testOneOverrideOption = 1;
		testOneOption = new String[] {"Option A"};
		testFourOptions = new String[] {"Option P", "Option Q", "Option R", "Option S"};	
	}

	// when_given_then format of test methods
	
	@Test(expected = Exception.class)
	public void ScannerIn_NoQuestionNoOption_Exception() throws Exception {
		result = testConsoleWithError.getAnswerFromConsole(null, null);
	}

	@Test(expected = Exception.class)
	public void ScannerIn_NoQuestion_OneOption_Exception() throws Exception {
		result = testConsoleWithError.getAnswerFromConsole(null, testOneOption);;
	}

	@Test(expected = Exception.class)
	public void ScannerIn_Question_NoOption_Exception() throws Exception {
		result = testConsoleWithError.getAnswerFromConsole("Question with no option", null);
	}

	@Ignore
	public void ScannerIn_QuestionOneOption_Ok() throws Exception {
		// test
		result = testConsoleWithOneOption.getAnswerFromConsole("Question with one option", testOneOption);
		
		// assert
		assertEquals(result, 1);
		
		// test
		result = testConsoleWithOneOption.getAnswerFromConsole("Question with one option", testOneOption);
		
		// assert
		assertEquals(result, 1);
	}

	@Ignore
	public void ScannerIn_QuestionFourOptions_Ok() throws Exception {
		// test
		result = testConsoleWithFourOptions.getAnswerFromConsole("Question with four options", testFourOptions);
		
		// assert
		assertEquals(result, 3);
	}

	@Test(expected = Exception.class)
	public void AutoIn_QuestionFourOptions_ZeroOverride() throws Exception {
		// test
		result = testConsoleWithFourOptions.autoAnswerOnConsole("Auto question with four options", testFourOptions, testNoOverrideOption);
		
		// assert
		assertEquals(result, 0);
	}
	
	@Test
	public void AutoIn_QuestionFourOptions_OneOverride() throws Exception {
		// test
		result = testConsoleWithFourOptions.autoAnswerOnConsole("Question with four options", testFourOptions, testOneOverrideOption);
		
		// assert
		assertEquals(result, 1);
	}
	

}
