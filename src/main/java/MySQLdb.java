
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class MySQLdb {
    private Connection connection;

    private final String HOST;
    private final String USERNAME;
    private final String PASSWORD;

    public MySQLdb(String host, String username, String password) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        HOST = host;
        USERNAME = username;
        PASSWORD = password;
    }
    private void connect() throws SQLException {
        connection = DriverManager.getConnection(HOST,USERNAME,PASSWORD);

        if (connection.isClosed()) {
            throw new RuntimeException(String.format("Failed to connect to the database(%s) with User: %s and Password : %s",HOST,USERNAME,PASSWORD));
        }else{
            System.out.println("Connected Successfully!");
        }
    }
    private void disconnect() throws SQLException {
        if(connection.isClosed()){
            System.out.println("Connection was already closed");
        }
        else{
            connection.close();
            if(connection.isClosed()) {
                System.out.println("Connection Closed!");
            }else{
                throw new RuntimeException(String.format("Failed to disconnect from the database(%s) with User: %s and Password : %s",HOST,USERNAME,PASSWORD));
            }
        }
    }
    public JSONArray runQuery(String query) throws SQLException {
        System.out.println("Query="+query);
        try{
            connect();
            PreparedStatement statement = connection.prepareStatement(query);
            return MySQLdb.convertSQLResultSetToJsonArray(statement.executeQuery());
        }finally {
            disconnect();
        }
    }
    public void runUpdate(String query) throws SQLException {
        System.out.println("Query="+query);
        try{
            connect();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        }finally {
            disconnect();
        }
    }
    public static JSONArray convertSQLResultSetToJsonArray(ResultSet resultSet) throws SQLException {

        JSONArray jsonArray = new JSONArray();

        if (resultSet != null) {
            System.out.println("Converting ResultSet to JSONArray...");

            ResultSetMetaData metaData = resultSet.getMetaData();
            int numOfColumns = metaData.getColumnCount();

            while (resultSet.next()) {

                JSONObject jsonObject = new JSONObject();
                for (int i = 1; i < numOfColumns + 1; i++) {

                    String column_name = metaData.getColumnName(i);
                    int column_type = metaData.getColumnType(i);

                    if (column_type == Types.ARRAY) {
                        jsonObject.put(column_name,resultSet.getArray(column_name));
                    } else if(column_type == Types.BIGINT){
                        jsonObject.put(column_name,resultSet.getInt(column_name));
                    }else if(column_type == Types.BOOLEAN){
                        jsonObject.put(column_name,resultSet.getBoolean(column_name));
                    }else if(column_type == Types.BLOB){
                        jsonObject.put(column_name,resultSet.getBlob(column_name));
                    }else if(column_type == Types.DOUBLE){
                        jsonObject.put(column_name,resultSet.getDouble(column_name));
                    }else if(column_type == Types.FLOAT){
                        jsonObject.put(column_name,resultSet.getFloat(column_name));
                    }else if(column_type == Types.INTEGER){
                        jsonObject.put(column_name,resultSet.getInt(column_name));
                    }else if(column_type == Types.NVARCHAR){
                        jsonObject.put(column_name,resultSet.getNString(column_name));
                    }else if(column_type == Types.VARCHAR){
                        jsonObject.put(column_name,resultSet.getString(column_name));
                    }else if(column_type == Types.VARBINARY){
                        jsonObject.put(column_name,resultSet.getString(column_name));
                    }else if(column_type == Types.TINYINT){
                        jsonObject.put(column_name,resultSet.getInt(column_name));
                    }else if(column_type == Types.SMALLINT){
                        jsonObject.put(column_name,resultSet.getInt(column_name));
                    }else if(column_type == Types.DATE){
                        jsonObject.put(column_name,resultSet.getDate(column_name));
                    }else if(column_type == Types.TIMESTAMP){
                        jsonObject.put(column_name,resultSet.getTimestamp(column_name));
                    }else {
                        jsonObject.put(column_name,resultSet.getObject(column_name));
                    }
                }
                jsonArray.put(jsonObject);
            }
        }else {
            System.out.println("No Results in the resultSet");
        }

        System.out.println("No: of records in json : " + jsonArray.length());
        for (Object json : jsonArray) {
            System.out.println(json);
        }
        return jsonArray;
    }
}
