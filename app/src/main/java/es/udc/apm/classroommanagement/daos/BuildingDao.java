package es.udc.apm.classroommanagement.daos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

import es.udc.apm.classroommanagement.objects.BuildingInfo;
import es.udc.apm.classroommanagement.daos.DBconnection;

/**
 * Created by david on 12/03/17.
 */

public class BuildingDao {

    private static final String URL = "jdbc:mysql://classroommanagement.cmovejjwtc0u.us-east-1.rds.amazonaws.com:3306/";
    private static final String DB_NAME = "ClassRoomDB";
    private static final String USER_NAME = "crm";
    private static final String PASSWORD = "Apm+2016";
    private static final String URL_KEY = "UrlKey";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private DBconnection dBconnection;
    private Queue<ResultSet> queue;

    public BuildingDao() {

        this.queue = new PriorityQueue<ResultSet>();
        this.dBconnection = new DBconnection(URL, USER_NAME, PASSWORD, DB_NAME, queue);
    }

    public ArrayList<BuildingInfo> getBuildings()  {
        ArrayList<BuildingInfo> returnList = new ArrayList<BuildingInfo>();
        dBconnection.set_query("SELECT * FROM TBUILDING");

        int expectedResults = 1;
        this.dBconnection.start();


        int receivedResults = 0;
        while (receivedResults < expectedResults) {
            try {
                if (!queue.isEmpty()) {
                    ResultSet rows = queue.poll();

                    while (rows.next()) {
                        
                    }

                    receivedResults++;
                }

                    Thread.sleep(1000);
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
        }



        return returnList;
    }
}
