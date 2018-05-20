/**
 * cd D:/Work/Projects/workspace/CardGamesAPI
 * javac src/test/java/nl/knikit/cardgames/training/MySql.java
 * execute with: java -cp src/test/java nl.knikit.cardgames.training.MySql
 * 1/2/3 returns a boolean, int or resultset
 */
package nl.knikit.cardgames.training;
import java.sql.*;
public class MySql {
	public static void someSql(Connection con, String dbName) throws SQLException {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			boolean isResultSetPresent = stmt.execute("insert into " + dbName + ".PRODUCT values ('Product1', 50).PRODUCT"); // 1
			int rowsAffected = stmt.executeUpdate("insert into " + dbName + ".PRODUCT values ('Product1', 50).PRODUCT"); // 2
			ResultSet rs = stmt.executeQuery("select SALE from " + dbName + ".PRODUCT"); // 3
			while (rs.next()) { int sale =+ rs.getInt("SALE"); }
		} catch (SQLException e ) { System.out.println(e);
		} finally { if (stmt != null) { stmt.close(); } }
	}
}
