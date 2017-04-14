package es.udc.apm.classroommanagement.services;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.SQLException;

import es.udc.apm.classroommanagement.dao.UserDAO;
import es.udc.apm.classroommanagement.model.User;

/**
 * Created by danib on 20/03/2017.
 */

public class UserService extends AsyncTask<String, Void, User> {

    //Constants
    private static final String TAG = UserService.class.getSimpleName();
    private UserDAO userDao;

    public UserService() {
    }

    @Override
    protected User doInBackground(String... params) {
        if (userDao == null) {
            try {
                userDao = new UserDAO();
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        String userGoogleID = params[0];
        User user = getUser(userGoogleID);
        return user;
    }

    private User getUser(String googleID) {
        return userDao.getUser(googleID);
    }
}
