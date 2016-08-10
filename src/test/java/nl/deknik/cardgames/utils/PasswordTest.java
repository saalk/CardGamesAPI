package nl.deknik.cardgames.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.deknik.cardgames.utils.Password.SymmetryEncryption;

public class PasswordTest {

	
	SymmetryEncryption testInputWrongSym; 
	SymmetryEncryption testInputCorrectSym;
	String testInputPassword, testEncodedPassword;
	String result;

	Password test = new Password();
	
	@Before
	public void setUp() throws Exception {

		testInputWrongSym = SymmetryEncryption.AES;
		testInputCorrectSym = SymmetryEncryption.BLOWFISH;
		
		testInputPassword = "wachtwoord";
		testEncodedPassword = "1v/9h9F7jVFOS2vcr+gZnw==";
		
	}

	// when_given_then format of test methods
	
	@Test(expected = Exception.class)
	public void Password_EncodeWithAES_Exception() throws Exception {
		result = test.decode(testInputWrongSym, testInputPassword);
	}
	
	@Test
	public void Password_EncodeWithBLOWFISH_Ok() throws Exception {
		result = test.encode(testInputCorrectSym, testInputPassword);
		assertEquals(result, testEncodedPassword);
	}
	
	@Test
	public void Password_DecodeWithBLOWFISH_Ok() throws Exception {
		result = test.decode(testInputCorrectSym, testEncodedPassword);
		assertEquals(result, testInputPassword);
	}	
	
}
