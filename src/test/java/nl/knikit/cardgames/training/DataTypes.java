package nl.knikit.cardgames.training;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataTypes {
	
	private static String printMe = "static string printed";
	
	public static void main(String[] args) {
		// 1: compile with:
		// cd D:/Work/Projects/workspace/CardGamesAPI
		// javac src/test/java/nl/knikit/cardgames/training/DataTypes.java
		// 2: execute with: java -cp src/test/java nl.knikit.cardgames.training.DataTypes
		// cp = classpath, a path of directories in which .class files are located
		// 3: change the "//" in the result,
		
		/* one stack per thread, heap is a shared area
		* - stack: method and local data area, return and parameter storage
		* - heap: object and array data area
		* */
		
		/* static field via null object */
		DataTypes object = null;
		System.out.println("Print a static string in class from main() after creating null object for class is ok: " + object.printMe); // is this printed
		
				
		/* static methods in super and sub overriding */
		staticMethodNoOverride();
		
		/* data and array examples */
		DataTypes types = new DataTypes();
		types.dataTypes();
		types.arrayTypes();
		types.collectionTypes(0);
		
	}
	
	public static void staticMethodNoOverride() {
		
		
		DataTypes subOverride = new SubDataType(); // ok
		SubDataType sub1 = new SubDataType(); // ok
		// SubDataType sub2 = new DataTypes(); // not ok
		
		// static method of super class will be called,
		// even though object is of sub-class SubDataType
		subOverride.printCategory();
		
		//static method of sub class will be called
		sub1.printCategory();
	}
	
	public static void printCategory() {
		System.out.println("inside super class static method");
	}
	
	
	public void collectionTypes(int test1) {
		
		++test1;
		//  test2++; // error since test2 is not initialized
		
		List<String> toppings = new ArrayList<>();
		//toppings = new String[3];  // or in one statement
		toppings.add("Cheese");
		toppings.add("Pepperoni");
		toppings.add("Black Olives");
		toppings.add("Pepperoni");
		System.out.println("Given an ArrayList: " + toppings.toString());
		toppings.remove("Pepperoni");

		System.out.println("remove one of 2 of the same strings (Pepperoni) from an ArrayList results in removing the first found: " + toppings.toString());
		
		toppings.add("Cheese");
		System.out.println("Givenan ArrayList: " + toppings.toString());
		toppings.remove("Cheese");
		System.out.println("remove one of 2 of the same strings (Cheese) from an ArrayList results in removing the first found: " + toppings.toString());
		
		List numbers = new ArrayList<>();
		//numbers = no <> gives a .. for numbers
		numbers.add(7);
		numbers.add(2);
		numbers.add(7);
		numbers.add(8);
		System.out.println("Given an ArrayList: " + numbers.toString());
		numbers.remove(2);
		
		System.out.println("remove one of 2 of the same ints (2) from an ArrayList results in removing the index 2!: " + numbers.toString());
		
		numbers.add(2);
		System.out.println("Given an ArrayList: " + numbers.toString());
		// numbers.remove(7); //error
		// System.out.println("remove one of 2 of the same strings (7) from an ArrayList results in removing the index 7!: " + numbers.toString());
		
	}
	
	public void arrayTypes() {
		
		String[] toppings = new String[3];
		//toppings = new String[3];  // or in one statement
		toppings[0] = "Cheese";
		toppings[1] = "Pepperoni";
		toppings[2] = "Black Olives";
		try {
			toppings[3] = "Tomatoes"; // error
		} catch (Exception e) {
			System.out.println("Exception in thread main java.lang.ArrayIndexOutOfBoundsException: 3");
		}
		
		String[] aArray = new String[5];
		String[] bArray = {"a", "b", "c", "d", "e"};
		String[] cArray = new String[]{"a", "b", "c", "d", "e"};
		int[] intArray = {1, 2, 3, 4, 5};
		String intArrayString = Arrays.toString(intArray);
		
		// print directly will print reference value
		System.out.println(intArray);
		
		System.out.println(intArrayString);
		// [1, 2, 3, 4, 5]
		
		// Create ArrayList from array
		ArrayList<String> arrayList2 = new ArrayList<String>(Arrays.asList(bArray));
		
		// Check if an array contains a certain value
		boolean b = Arrays.asList(bArray).contains("a");
		
		// Concatenate two arrays / reverse / remove element -> ArrayList
		int[] combinedIntArray = ArrayUtils.addAll(intArray, intArray);
		ArrayUtils.reverse(intArray);
		int[] removed = ArrayUtils.removeElement(intArray, 3);//create a new array
		
		
		// Joins the elements of the provided array into a single String
		String j = StringUtils.join(new String[]{"a", "b", "c"}, ", ");
		System.out.println(j);
		// a, b, c
		
		// Convert ArrayList to Array
		String[] stringArray = {"a", "b", "c", "d", "e"};
		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(stringArray));
		String[] stringArr = new String[arrayList.size()];
		arrayList.toArray(stringArr);
		for ( String s : stringArr) {
			System.out.println(s);
		}
		
		// 2-dim array
		int[][] arr = new int[3][];
		arr[0] = new int[3];
		arr[1] = new int[5];
		arr[2] = new int[4];
		
		
	}
	
	public void dataTypes() {
		char letterA = 'A';     // 16 bits unitcode, minimum value is '\u0000' (or 0)
		// always use single quotes
		
		byte b = 100;           // 8 bits, 128 max - default value is 0
		//byte b = 100b;        // error
		// byte 12 -> 0000 1100 -> .... 8421
		// byte ~12 -> inverts, & and | can also be used, >> and << shifts the bits
		System.out.println("byte: " + b); // print 100
		
		byte b1 = 127 - 0; // ok
		// byte b2 = 128 - 0; // error
		byte b3 = 128 - 1; // ok
		
		
		short s = 12345;        // 16 bits, 32000 max - default value is 0
		System.out.println("short: " + s); // print 100
		
		short s1 = 2;
		short s2 = 3;
		short s3 = s2;
		// short s4 = s1 + s2;   // error !
		// right-hand side of a arithmetic evaluates to int by default!
		// due to support for multi platform math
		
		b = (byte) s;
		System.out.println("casting a short 12345 to byte: " + b); // print 57
		
		int binary = 0b100;    // base-2 just start with 0b, only use 1 or 0
		int decimal = 1234;
		int octal = 0144;       // base-8 just start with zero, no 8 or 9 are possible
		int hexa = 0xff;       // base-16 just start with 0X, also use a-f
		
		int i = 100000;         // 32 bits - default value is 0
		
		System.out.println("Print 'tekst + 3 + 4' does not give 7! : " + 3 + 4); // print 34! since you start with a string
		
		long l = 100000L;       // 64 bits - default value is 0L
		float f1 = 234.5f;      // 32 bits - default value is 0.0f, can have NaN
		double d1 = 123.4d;     // 64 bits - default value is 0.0d
		
		boolean bool = true;    //Default value is false
	}
}

class SubDataType extends DataTypes {
	
	// SubDataType sub1 = new SubDataType(); // not ok already defined in scope
	// SubDataType sub2 = new DataTypes(); // not ok
	
	public static void printCategory() { System.out.println("inside sub class static method"); }
}
