package nl.knikit.cardgames.event;

import nl.knikit.cardgames.model.Player;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;

/*
To build an interceptor, you can either implement Interceptor class directly or extend
EmptyInterceptor class. Following will be the simple steps to use Hibernate Interceptor
functionality.
*/
public class MyInterceptor extends EmptyInterceptor {
	private int updates;
	private int creates;
	private int loads;
	
	public void onDelete(Object entity,
	                     Serializable id,
	                     Object[] state,
	                     String[] propertyNames,
	                     Type[] types) {
		// do nothing
	}
	
	// This method is called when Employee object gets updated.
	public boolean onFlushDirty(Object entity,
	                            Serializable id,
	                            Object[] currentState,
	                            Object[] previousState,
	                            String[] propertyNames,
	                            Type[] types) {
		if (entity instanceof Player) {
			System.out.println("Interceptor: Update Operation for " + entity.getClass().toString());
			return true;
		}
		return false;
	}
	
	public boolean onLoad(Object entity,
	                      Serializable id,
	                      Object[] state,
	                      String[] propertyNames,
	                      Type[] types) {
		// do nothing
		return true;
	}
	
	// This method is called when Player object gets created.
	public boolean onSave(Object entity,
	                      Serializable id,
	                      Object[] state,
	                      String[] propertyNames,
	                      Type[] types) {
		if (entity instanceof Player) {
			System.out.println("Interceptor - Create Operation for " + entity.getClass().toString());
			return true;
		}
		return false;
	}
	
	//called before commit into database
	public void preFlush(Iterator iterator) {
		System.out.println("Interceptor - preFlush before commit");
	}
	
	//called after committed into database
	public void postFlush(Iterator iterator) {
		System.out.println("Interceptor - postFlush after commit");
	}
}
