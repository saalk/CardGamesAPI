/**
 * cd D:/Work/Projects/workspace/CardGamesAPI
 * javac src/test/java/nl/knikit/cardgames/training/MyStatic.java
 * execute with: java -cp src/test/java nl.knikit.cardgames.training.MyStatic
 * cp = classpath, a path of directories in which .class files are located
 * String args[] / String... xyz is also possible
 * one stack (method and local data area) per thread, heap is 0bject and array data area
 *
 * 1/2 package, import, util is all without an s, a * does not import sub packages
 * 3/4/5/6/7 what is instance, local, class, whats strange with 6
 * 8 can this be Char[] or can args or main have a different name,
 * what if public, static or void is omitted?
 * 9/10/11 explain, lok at 3
 * 12/13/14 what is the result and why, why map to double and is result .0 or without the 0.
 * 15 alternatives like map, mapToInt, { {1,2}, {3,4}, {5,6} } -> flatMap -> {1,2,3,4,5,6}
 */
package nl.knikit.cardgames.training; // 1
import java.util.*; // 2
import java.util.function.Predicate; // 2
class MyStatic {
	static double price; // 3
	String color;
	{ 	int price = 1; // 4
	}
	MyStatic(String color, double price) { // 5
		this.price = price; // 6
		this.color = color;
		price = 2; // 7
		System.out.println("this.price = " + this.price + " price = " + price);
	}
	public static void main(String[] args) { // 8
		List<MyStatic> myStatics = new ArrayList<>();
		myStatics.add(new MyStatic("red", 3)); // 9
		myStatics.add(new MyStatic("yellow", 4));
		myStatics.add(new MyStatic("red", 5)); // 10
		myStatics.remove(new MyStatic("blue", 6)); // 11
		System.out.println("Total: " + calcPrice(myStatics, e -> e.getColor() == "red") ); // 12
		System.out.println("Total: " + calcPrice(myStatics, e -> e.getColor() == "yellow") ); // 13
		System.out.println("Total: " + calcPrice(myStatics, e -> e.getColor() == "blue") ); // 14
	}
	public String getColor() { return color; }
	public double getPrice() { return price; }
	public static double calcPrice(List<MyStatic> myStatics, Predicate<MyStatic> predicate) {
		return myStatics.stream() // 13
				       .filter(predicate) // or use e -> e.getColor() == "red")
				       .mapToDouble(e -> e.getPrice()) // 15
				       .sum();
	}
}
