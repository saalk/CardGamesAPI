package nl.knikit.cardgames.commons.util;

import nl.knikit.cardgames.VO.CardGame;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StackableResponses{
	
	static protected Map<String, LinkedList<Object>> responses = new HashMap<>();
	
	static public void push(Object object) {
		if (responses.isEmpty()) {init();}
		
		String entity = getInstanceOf(object).toLowerCase();
		LinkedList<Object> linkedList = responses.get(entity);
		linkedList.addLast(object);
		responses.put(entity, linkedList);
	}
	
	static public Object peekLast(String object) {
		LinkedList<Object> linkedList = responses.get(object.toLowerCase());
		return linkedList.getLast();
	}
	
	static public Object peekAt(String object, int number) {
		LinkedList<Object> linkedList = responses.get(object.toLowerCase());
		return linkedList.get(number-1);
	}
	
	static public Object peekAndPop(String object) {
		LinkedList<Object> linkedList = responses.get(object.toLowerCase());
		Object objectFromLinkedList = linkedList.getLast();
		linkedList.removeLast();
		responses.remove(object.toLowerCase());
		responses.put(object.toLowerCase(), linkedList);
		return objectFromLinkedList;
	}
	
	static public void pop(String object) {
		LinkedList<Object> linkedList = responses.get(object.toLowerCase());
		linkedList.removeLast();
		responses.remove(object.toLowerCase());
		responses.put(object.toLowerCase(), linkedList);
	}
	
	static public void pop(Object object) {
		String entity = getInstanceOf(object).toLowerCase();
		
		LinkedList<Object> linkedList = responses.get(entity.toLowerCase());
		linkedList.removeLast();
		responses.remove(entity.toLowerCase());
		responses.put(entity.toLowerCase(), linkedList);
	}
	
	static private String getInstanceOf(Object object) {
		if (object instanceof CardGame) {
			return "cardgames";
		} else if (object instanceof Game) {
			return "games";
		} else if (object instanceof Player) {
			return "players";
		} else if (object instanceof Hand) {
			return "hands";
		} else if (object instanceof Deck) {
			return "decks";
		} else if (object instanceof Casino) {
			return "casinos";
		}
		return "cardgames";
	}
	
	static private void init() {
		responses.put("cardgames", new LinkedList<>());
		responses.put("games", new LinkedList<>());
		responses.put("players", new LinkedList<>());
		responses.put("decks", new LinkedList<>());
		responses.put("hands", new LinkedList<>());
		responses.put("casinos", new LinkedList<>());
	}
}
