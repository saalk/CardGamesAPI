package nl.knikit.cardgames.commons.training;

import java.util.*;

public class CollectionsDemo {
	public static Collection<String> goArrayList() {
		// ArrayList
		List<String> test = new ArrayList<>();
		test.add("Zara");
		test.add("Mark");
		test.add("Alice");
		test.add("Mark"); // allows double or not?
		test.remove("Mark"); // which one is removed ?
		test.add("Mark");
		return test; // what is the order now on name or age?
	}
	
	public static Collection<String> goLinkedList() {
		// LinkedList
		List<String> test = new LinkedList<>();
		test.add("Zara");
		test.add("Mark");
		test.add("Alice");
		test.add("Mark"); // allows double or not?
		test.remove("Mark"); // which one is removed ?
		test.add("Mark");
		return test; // what is the order now on name or age?
	}
	
	public static Collection<String> goHashSet() {
		// HashSet
		Set<String> test = new HashSet<>();
		test.add("Zara");
		test.add("Mark");
		test.add("Alice");
		test.add("Mark"); // allows double or not?
		test.remove("Mark"); // which one is removed ?
		test.add("Mark");
		return test; // what is the order now on name or age?
	}
	
	public static Map<String, String> goHashMap() {
		// HashMap
		Map<String, String> test = new HashMap<String, String>();
		test.put("Zara", "8");
		test.put("Mark", "31");
		test.put("Alice", "12");
		test.put("Daisy", "14");
		test.put("Mark", "31");; // allows double or not?
		test.remove("Mark"); // which one is removed?
		test.put("Mark", "31");;
		return test; // what is the order now on name or age?
	}
}
