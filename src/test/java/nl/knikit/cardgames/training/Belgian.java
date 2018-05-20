/**
 * cd D:/Work/Projects/workspace/CardGamesAPI
 * javac src/test/java/nl/knikit/cardgames/training/Belgian.java
 * execute with: java -cp src/test/java nl.knikit.cardgames.training.Belgian
 * 4 class files are generated only one is callable
 *
 * 1 cannot be instantiated (compile time error), constructors are for use of super()
 * 2/6 explicit needed since dutch automatically has super(), public is useless protected is better
 * 3 belgian does not need explicit no-arg constructor
 * 4/9 belgian cannot restrict public (for default or private) or change signature or have return xyz
 * if 4 was static 9 would be hiding the buy in customer
 * 5 no need to implement buy, only concrete subclasses must
 * 7 belgian does not use the no-arg constructor of customer
 * 9 implement the abstract method = override -> annotation not really needed to tell compiler
 * 10 overloading the buy, this is a new method unique to Belgian
 * 11/12/13 order is 13/12/14/11 -> try with resources
 * Closeable extends AutoCloseable, and is specifically dedicated to IO streams not eg. sql
 */
package nl.knikit.cardgames.training;
import java.io.IOException;
abstract class Customer { // 1
	private String name;
	public Customer () { }  // 2
	public Customer (String name) { this.name = name; } // 3
	public abstract void buy(); // 4
}
abstract class Dutch extends Customer { // 5
//	public Dutch() { super(); } // 6
}
public class Belgian extends Customer {
	Belgian (){ super("Belg");} // 7
	@Override // 8
	public void buy(){ return; } // 9
	public String buy(String what){return "xyz";} // 10
	public static void main(String[] args) throws Exception {
		try (TestMe r = new TestMe()) { r.generalTest(); } // 13
		catch (IOException e) { System.out.println(" From Catch Block"); } // 14
		finally { System.out.println(" From Final Block"); } // 11
	}
}
class TestMe implements AutoCloseable {
	@Override
	public void close() throws Exception {
		System.out.println(" From Close"); // 12
	}
	public void generalTest() throws IOException {
		System.out.println(" GeneralTest "); // 13
		throw new IOException();
	}
}