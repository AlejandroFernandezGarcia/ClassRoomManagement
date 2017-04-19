package es.udc.apm.classroommanagement.services;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.udc.apm.classroommanagement.dao.UserDAO;
import es.udc.apm.classroommanagement.model.User;

/**
 * Created by danib on 20/03/2017.
 */

public class UserService {

    //Constants
    private static final String TAG = UserService.class.getSimpleName();
    private UserDAO userDao;

    public UserService() {
        userDao = new UserDAO();
    }

    public User getUser(String googleID) throws ExecutionException, InterruptedException {
        GetUserTask getUserTask = new GetUserTask();
        User user = getUserTask.execute(googleID).get();
        getUserTask = null;
        return user;
    }

    public void insertUser(String googleID, String name, String surname, String mail, short roleID) throws Exception {
        InsertUserTask insertUserTask = new InsertUserTask();
        List<String> params = new ArrayList<>();
        params.add(googleID);
        params.add(name);
        params.add(surname);
        params.add(mail);
        params.add(String.valueOf(roleID));
        int insertResult = insertUserTask.execute(params).get();
        insertUserTask = null;
        if (insertResult != 1) {
            throw new Exception("Error al insertar usuario");
        }
    }

    /**
     * Searches for the user by google ID.
     */
    private class GetUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            if (userDao == null) {
                userDao = new UserDAO();
            }
            String userGoogleID = params[0];
            User user = userDao.getUser(userGoogleID);
            return user;
        }
    }

    private class InsertUserTask extends AsyncTask<List<String>, Void, Integer> {

        @Override
        protected Integer doInBackground(List<String>... params) {
            if (userDao == null) {
                userDao = new UserDAO();
            }
            List<String> userDetails = params[0];
            return userDao.insertUser(userDetails.get(0), userDetails.get(1), userDetails.get(2), userDetails.get(3), Short.parseShort(userDetails.get(4)));

        }

    }


}
