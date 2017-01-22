package nl.knikit.cardgames.commons.training;

import org.junit.Test;

import static org.junit.Assert.*;

public class SuperSubDemoTest {
	@Test
	public void testSuperSubDemo() throws Exception {
		
		SuperSubDemo test = new SuperSubDemo();
		
		String output = test.testSuperSubDemo();
		System.out.println(" SupersubDemo Output");
		System.out.print("\t" + output);
		System.out.println();
	}
	
}