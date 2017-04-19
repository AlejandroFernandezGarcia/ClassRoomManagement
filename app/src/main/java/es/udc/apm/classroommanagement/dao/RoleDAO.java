package es.udc.apm.classroommanagement.dao;

import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.udc.apm.classroommanagement.model.Role;

/**
 * Created by danib on 21/03/2017.
 */

public class RoleDAO {

    //Constants
    private static final String TAG = RoleDAO.class.getSimpleName();
    private ConnectionManager connection;

    public RoleDAO() {
    }

    public List<Role> getAllRoles() {
        String query = "SELECT * FROM TROLE";
        List<Role> roles = new ArrayList<>();
        try {
            this.connection = new ConnectionManager();
            this.connection.executeQuery(query);
            while (connection.getResult().next()) {
                short id = connection.getResult().getShort("ROL_ID");
                String roleName = connection.getResult().getString("ROL_NAME");
                Role role = new Role(id, roleName);
                roles.add(role);
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
        return roles;

    }

}
