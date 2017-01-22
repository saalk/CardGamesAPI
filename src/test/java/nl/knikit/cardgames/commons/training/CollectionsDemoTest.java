package nl.knikit.cardgames.commons.training;

import org.junit.Test;

import java.util.Collection;
import java.util.Map;


public class CollectionsDemoTest {
	
	@Test
	public void testCollection() throws Exception {
		Collection<String> list;
		Map<String, String> map; // Collection is not a Map!
		
		list = CollectionsDemo.goArrayList();
		System.out.println(" ArrayList Elements");
		System.out.print("\t" + list);
		System.out.println();
		
		list = CollectionsDemo.goLinkedList();
		System.out.println(" LinkedList Elements");
		System.out.print("\t" + list);
		System.out.println();
		
		list = CollectionsDemo.goHashSet();
		System.out.println(" Set Elements");
		System.out.print("\t" + list);
		System.out.println();
		
		map = CollectionsDemo.goHashMap();
		System.out.println(" Map Elements");
		System.out.print("\t" + map);
		System.out.println();
	}
}