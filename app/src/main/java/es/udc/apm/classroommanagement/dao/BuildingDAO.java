package es.udc.apm.classroommanagement.dao;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.udc.apm.classroommanagement.model.Building;

public class BuildingDAO {

    private static final String TAG = BuildingDAO.class.getSimpleName();
    private ConnectionManager connection;

    public BuildingDAO() {
    }

    public List<Building> getAllBuildings()  {
        String query = "SELECT * FROM TBUILDING";
        List<Building> result = new ArrayList<Building>();

        try {
            this.connection = new ConnectionManager();
            this.connection.executeQuery(query);
            while (connection.getResult().next()) {
                short id = connection.getResult().getShort("BUILD_ID");
                double latitude = connection.getResult().getDouble("BUILD_LATITUDE");
                double longitude = connection.getResult().getDouble("BUILD_LONGITUDE");
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
