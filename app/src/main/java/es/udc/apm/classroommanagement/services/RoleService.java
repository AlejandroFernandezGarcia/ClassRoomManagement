package es.udc.apm.classroommanagement.services;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import es.udc.apm.classroommanagement.dao.RoleDAO;
import es.udc.apm.classroommanagement.model.Role;

/**
 * Created by danib on 21/03/2017.
 */

public class RoleService extends AsyncTask<Void, Void, List<Role>> {

    //Constants
    private static final String TAG = RoleService.class.getSimpleName();
    private RoleDAO roleDAO;

    public RoleService() {
    }

    @Override
    protected List<Role> doInBackground(Void... params) {
        if (this.roleDAO == null) {
            try {
                this.roleDAO = new RoleDAO();
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        List<Role> roles = getAllRoles();
        return roles;
    }

    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }
}
