//package nl.knikit.cardgames.model;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//
//import org.junit.Ignore;
//import org.junit.runners.MethodSorters;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class PlayerOldTest {
//
//	// make instance variables
//	private PlayerOld testHumanPlayerOld1, testAiPlayerOld2, testAiPlayerOld3;
//
//	@Before
//	public void setUpBeforeEachTest() throws IOException, URISyntaxException {
//
//		// cannot make instance variables here ?
//
//		// initialize the variables
//		testHumanPlayerOld1 = new PlayerOld(Avatar.ELF, AiLevel.MEDIUM ,true);
//		testAiPlayerOld2 = new PlayerOld(Avatar.ELF, AiLevel.MEDIUM, false);
//		testAiPlayerOld3 = new PlayerOld(Avatar.ELF, AiLevel.MEDIUM, false);
//
//	}
//
//	@Ignore
//	@Test
//	public void test1_Make3PlayersIdsShouldBe123() {
//
//		// run this test first since the PlayerOld id starts with 1
//
//		// verify
//		assertEquals("PlayerOld ids not 1", 1, testHumanPlayerOld1.getId());
//		assertEquals("PlayerOld ids not 2", 2, testAiPlayerOld2.getId());
//		assertEquals("PlayerOld ids not 3", 3, testAiPlayerOld3.getId());
//	}
//	@Ignore
//	@Test
//	public void testMake3PlayersAllShouldHaveNames() throws IOException {
//
//		// verify
//		assertNotNull("PlayerOld name is null", testHumanPlayerOld1.getAlias());
//		assertNotNull("PlayerOld name is null", testAiPlayerOld2.getAlias());
//		assertNotNull("PlayerOld name is null", testAiPlayerOld3.getAlias());
//	}
//	@Ignore
//	@Test
//	public void testMake3PlayersOneIsHuman() {
//
//		// verify
//		assertTrue("Number of Humans players not correct",
//				testHumanPlayerOld1.human() && !testAiPlayerOld2.human() && !testAiPlayerOld3.human());
//	}
//
//}
