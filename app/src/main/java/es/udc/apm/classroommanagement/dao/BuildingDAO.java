package es.udc.apm.classroommanagement.dao;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

import es.udc.apm.classroommanagement.model.Building;

/**
 * Created by david on 12/03/17.
 */

public class BuildingDAO {

    private static final String TAG = BuildingDAO.class.getSimpleName();

    private ConnectionManager connection;

    public BuildingDAO() {
    }

    public ArrayList<Building> getAllBuildings()  {
        ArrayList<Building> result = new ArrayList<Building>();
        String query = "SELECT * FROM TBUILDING";

        try {
            this.connection = new ConnectionManager();
            this.connection.executeQuery(query);
            while (connection.getResult().next()) {
                short id = connection.getResult().getShort("BUILD_ID");
                double latitude= connection.getResult().getShort("BUILD_LATITUDE");
                double longitude= connection.getResult().getShort("BUILD_LONGITUDE");
                String address= connection.getResult().getString("BUILD_ADDRES");
                String name = connection.getResult().getString("BUILD_NAME");
                int zipcode = connection.getResult().getInt("BUILD_ZIPCODE");
                String region = connection.getResult().getString("BUILD_REGION");
                String country = connection.getResult().getString("BUILD_COUNTRY");
                String phone = connection.getResult().getString("BUILD_PHONE");
                Building building = new Building(id,latitude,longitude,name,phone,address,zipcode,region,country);
                result.add(building);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                this.connection.closeConnection();
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }
}
