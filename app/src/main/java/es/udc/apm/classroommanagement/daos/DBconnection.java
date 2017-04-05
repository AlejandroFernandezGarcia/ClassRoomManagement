package es.udc.apm.classroommanagement.daos;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;


class DBconnection extends Thread {

    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private String url;
    private String login;
    private String passwd;
    private String db_name;
    private String query;

    private final Queue<ResultSet> results;

    public DBconnection(String url, String login, String passwd, String db_name, Queue<ResultSet> results){

        this.url = url;
        this.login = login;
        this.passwd = passwd;
        this.db_name = db_name;
        this.query = "";
        this.results = results;
    }

    public void set_query(String query){
        this.query = query;
    }


    public void run() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url+db_name, login, passwd);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(this.query);
            results.add(rs);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    }
