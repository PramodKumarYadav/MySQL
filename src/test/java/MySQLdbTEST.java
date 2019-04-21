import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class MySQLdbTEST {

    @Test
    public void runQuerySQLdbTEST() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        // On command line write : cat /etc/mysql/mysql.cnf  (to get the ip:port address on which mysql is running)
        MySQLdb mySQLdb = new MySQLdb("jdbc:mysql://10/0.0.121:1234/sa","myusername","mypassword");

        JSONArray jsonArrayResults = mySQLdb.runQuery("select * from qa.RESULTS");
        for (Object result : jsonArrayResults) {
            System.out.println(result);
        }
    }

    @Test
    public void runUpdateSQLdbTEST() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        // On command line write : cat /etc/mysql/mysql.cnf  (to get the ip:port address on which mysql is running)
        MySQLdb mySQLdb = new MySQLdb("jdbc:mysql://10/0.0.121:1234/sa","myusername","mypassword");

        mySQLdb.runUpdate("INSERT INTO RESULTS(test_suite, test_name, test_status) " +
                "values (\"Test Suite 1\", \"Test Name 1\",\"PASS\")");

        JSONArray jsonArrayResults = mySQLdb.runQuery("select * from qa.RESULTS");
        for (Object result : jsonArrayResults) {
            System.out.println(result);
        }
    }
}
