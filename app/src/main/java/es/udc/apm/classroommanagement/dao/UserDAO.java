package es.udc.apm.classroommanagement.dao;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

import es.udc.apm.classroommanagement.model.User;

/**
 * Created by danib on 20/03/2017.
 */

public class UserDAO {

    //Constants
    private static final String TAG = UserDAO.class.getSimpleName();
    private ConnectionManager connection;

    public UserDAO() {
    }

    public User getUser(String googleID) {
        String query = "SELECT * FROM TUSER WHERE TUSER.USER_GOOGLE_ID=".concat(googleID);
        User user = null;
        try {
            this.connection = new ConnectionManager();
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

    public int insertUser(String googleID, String name, String surname, String mail, short roleID) {
        String insert = "INSERT INTO TUSER (USER_GOOGLE_ID,USER_NAME,USER_LASTNAME,USER_MAIL,ROL_ID) VALUES (";
        StringBuilder insertBuilder = new StringBuilder(insert);
        String paramsArray[] = new String[4];
        paramsArray[0] = googleID;
        paramsArray[1] = name;
        paramsArray[2] = surname;
        paramsArray[3] = mail;
        String join = "'" + StringUtils.join(paramsArray, "','") + "',";
        insertBuilder.append(join);
        insertBuilder.append(roleID).append(")");
        int res = -1;
        try {
            this.connection = new ConnectionManager();
            res = this.connection.insert(insertBuilder.toString());
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                this.connection.closeConnection();
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return res;
    }

    public int updateUser(short id, String name, String surname, short roleID) {
        String update = "UPDATE TUSER SET";
        StringBuilder updateBuilder = new StringBuilder(update);
        updateBuilder.append(" USER_NAME = ").append("'").append(name).append("'");
        updateBuilder.append(", USER_LASTNAME = ").append("'").append(surname).append("'");
        updateBuilder.append(", ROL_ID = ").append(roleID);
        updateBuilder.append(" WHERE USER_ID = ").append(id);
        int res = -1;
        try {
            this.connection = new ConnectionManager();
            res = this.connection.update(updateBuilder.toString());
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                this.connection.closeConnection();
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return res;
    }
}
