package nl.knikit.cardgames.event;

import nl.knikit.cardgames.model.Player;

import java.util.List;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ManagePlayerEvents {
	private static SessionFactory factory;
	
	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		ManagePlayerEvents MP = new ManagePlayerEvents();
		
		/* Add few Player records in database */
		Integer empID1 = MP.addPlayer("Zara", "Ali", 1000);
		Integer empID2 = MP.addPlayer("Daisy", "Das", 5000);
		Integer empID3 = MP.addPlayer("John", "Paul", 10000);
		
		/* List down all the employees */
		MP.listPlayers();
		
		/* Update employee's records */
		MP.updatePlayer(empID1, 5000);
				
		/* Delete an employee from the database */
		MP.deletePlayer(empID2);
		
		/* List down new list of the employees */
		MP.listPlayers();
	}
	
	/* Method to CREATE an employee in the database */
	public Integer addPlayer(String alias, String avatar, int cubits) {
		Session session = factory.openSession();
		session.sessionWithOptions().interceptor(new MyInterceptor());
		
		Transaction tx = null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			Player player = new Player();
			player.setAlias(alias);
			player.setCubits(cubits);
			
			employeeID = (Integer) session.save(player);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}
	
	/* Method to READ all the employees */
	public void listPlayers() {
		Session session = factory.openSession();
		session.sessionWithOptions().interceptor(new MyInterceptor());
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List employees = session.createQuery("FROM Player").list();
			for (Iterator iterator =
			     employees.iterator(); iterator.hasNext(); ) {
				Player employee = (Player) iterator.next();
				System.out.print("First Name: " + employee.getAlias());
				System.out.println(" Salary: " + employee.getCubits());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/* Method to UPDATE salary for an employee */
	public void updatePlayer(Integer PlayerID, int cubits) {
		Session session = factory.openSession();
		session.sessionWithOptions().interceptor(new MyInterceptor());
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Player employee = session.get(Player.class, PlayerID);
			employee.setCubits(cubits);
			session.update(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/* Method to DELETE an employee from the records */
	public void deletePlayer(Integer PlayerID) {
		Session session = factory.openSession();
		session.sessionWithOptions().interceptor(new MyInterceptor());
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Player employee = session.get(Player.class, PlayerID);
			session.delete(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}