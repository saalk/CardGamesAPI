package nl.knikit.cardgames.commons.training;

public class SuperSubDemo {
	
	public String testSuperSubDemo() {
		Subclass s = new Subclass();
		return s.printMethod();
	}
	
	class Superclass {
		public String printMethod() {
			return "Printed in Superclass.";
		}
	}
	
	class Subclass extends Superclass {
		// overrides printMethod in Superclass
		public String printMethod() {
			String test = super.printMethod(); // calls the super print
			test += "Printed in Subclass.";    // adds this
			return test;
		}
	
	}
}
