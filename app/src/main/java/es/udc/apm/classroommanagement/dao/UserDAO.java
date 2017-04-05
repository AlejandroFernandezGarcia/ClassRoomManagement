package es.udc.apm.classroommanagement.dao;

import android.util.Log;

import java.sql.SQLException;

import es.udc.apm.classroommanagement.model.User;

/**
 * Created by danib on 20/03/2017.
 */

public class UserDAO {

    //Constants
    private static final String TAG = UserDAO.class.getSimpleName();
    private ConnectionManager connection;

    public UserDAO() throws SQLException {
        this.connection = new ConnectionManager();
    }

    public User getUser(String googleID) {
        String query = "SELECT * FROM TUSER WHERE TUSER.USER_GOOGLE_ID=".concat(googleID);
        User user = null;
        try {
            this.connection.executeQuery(query);
            if (connection.getResult().next()) {
                short id = connection.getResult().getShort("USER_ID");
                String googleId = connection.getResult().getString("USER_GOOGLE_ID");
                String name = connection.getResult().getString("USER_NAME");
                String lastName = connection.getResult().getString("USER_LASTNAME");
                String mail = connection.getResult().getString("USER_MAIL");
                short roleId = connection.getResult().getShort("ROL_ID");
                user = new User(id, googleId, name, lastName, mail, roleId);
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
        return user;
    }
}
