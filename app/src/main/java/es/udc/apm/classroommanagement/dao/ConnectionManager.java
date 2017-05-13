package es.udc.apm.classroommanagement.dao;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by danib on 20/03/2017.
 */

public class ConnectionManager {

    private static final String URL = "jdbc:mysql://classroommanagement.cmovejjwtc0u.us-east-1.rds.amazonaws.com:3306/";
    private static final String DB_NAME = "ClassRoomDB";
    private static final String USER_NAME = "crm";
    private static final String PASSWORD = "Apm+2016";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;

    public ConnectionManager() {
        if (connection == null) {
            this.connect();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSet getResult() {
        return result;
    }

    public void setResult(ResultSet result) {
        this.result = result;
    }

    private void connect() {
        ConnectionTask connectionTask = new ConnectionTask();
        connectionTask.execute();
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    //Methods to ineract with database

    public void executeQuery(String query) throws SQLException {
        this.statement = connection.createStatement();
        this.result = this.statement.executeQuery(query);
    }

    public int update(String update) throws SQLException {
        this.statement = connection.createStatement();
        return statement.executeUpdate(update);
    }

    public int delete(String delete) throws SQLException {
        this.statement = connection.createStatement();
        return statement.executeUpdate(delete);
    }

    public int insert(String insert) throws SQLException {
        this.statement = connection.createStatement();
        return statement.executeUpdate(insert);
    }

    private class ConnectionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL + DB_NAME, USER_NAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error " + e.getMessage());
            }
            return null;
        }
    }
}
