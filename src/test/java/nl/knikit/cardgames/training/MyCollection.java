/**
 * cd D:/Work/Projects/workspace/CardGamesAPI
 * javac src/test/java/nl/knikit/cardgames/training/MyCollection.java
 * execute with: java -cp src/test/java nl.knikit.cardgames.training.MyCollection
 * 1/2 List allows duplicates, remove removes the first found, index start at 0
 * however a null can only be removed with xyz.remove(null)
 * null is always the first, you can add more null (rest will all move +1 position)
 * 2 result is null, pepperoni
 * 3/4 Queue allows duplicates, peek does not remove, poll does remove, result is p1p1p2p2
 * LinkedList vs ArrayDeque -> List supports null, Queue does not, will throw run time exception
 * NB ArrayQueue does not exist
 * 5 here is says index but keep in mind its not using the number 2: 7, 2, 8, 2 is the result
 */
package nl.knikit.cardgames.training;
import java.util.*;
public class MyCollection {
	public static void main(String[] arg) {
		List<String> toppings = new ArrayList<>();
		toppings.add("Cheese"); toppings.add("Pepperoni"); toppings.add(null);
		toppings.add("Pepperoni"); toppings.remove("Pepperoni"); toppings.remove(0); // 1
		System.out.println(toppings.toString()); // 2 - puts nice [ , ] with toString
		
		Queue<String> products = new ArrayDeque<>(); // 3 - since java 6
		products.add("p1"); products.add("p2"); products.add("p2");
		System.out.print(products.peek()); System.out.print(products.poll()); // 4
		products.forEach(s -> System.out.print(s));

		List<Integer> numbers = new ArrayList<>();
		numbers.add(7); numbers.add(2);	numbers.add(7);	numbers.add(8);
		numbers.remove(2); numbers.add(2); // 5
		System.out.println(numbers.toString()); // numbers.remove(7); give run time error
	}
}
