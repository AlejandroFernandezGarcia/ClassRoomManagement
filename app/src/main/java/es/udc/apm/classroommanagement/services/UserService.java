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
    private static final UserDAO userDao = new UserDAO();

    public UserService() {
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

    public void updateUser(short id, String name, String surname, short roleID) throws Exception {
        UpdateUserTask updateUserTask = new UpdateUserTask();
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(id));
        params.add(name);
        params.add(surname);
        params.add(String.valueOf(roleID));
        int updateResult = updateUserTask.execute(params).get();
        updateUserTask = null;
        if (updateResult != 1) {
            throw new Exception("Error al actualizar los datos del usuario");
        }
    }

    /**
     * Searches for the user by google ID.
     */
    private class GetUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            String userGoogleID = params[0];
            return userDao.getUser(userGoogleID);
        }
    }

    private class InsertUserTask extends AsyncTask<List<String>, Void, Integer> {

        @SafeVarargs
        @Override
        protected final Integer doInBackground(List<String>... params) {
            List<String> userDetails = params[0];
            return userDao.insertUser(userDetails.get(0), userDetails.get(1), userDetails.get(2), userDetails.get(3), Short.parseShort(userDetails.get(4)));

        }

    }

    private class UpdateUserTask extends AsyncTask<List<String>, Void, Integer> {
        @SafeVarargs
        @Override
        protected final Integer doInBackground(List<String>... params) {
            List<String> userUpdateFields = params[0];
            return userDao.updateUser(Short.parseShort(userUpdateFields.get(0)), userUpdateFields.get(1), userUpdateFields.get(2), Short.parseShort(userUpdateFields.get(3)));

        }
    }


}
