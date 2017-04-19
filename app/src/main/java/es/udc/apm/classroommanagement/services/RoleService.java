package es.udc.apm.classroommanagement.services;

import android.os.AsyncTask;

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
        roleDAO = new RoleDAO();
    }

    @Override
    protected List<Role> doInBackground(Void... params) {
        if (this.roleDAO == null) {
            this.roleDAO = new RoleDAO();
        }
        List<Role> roles = getAllRoles();
        return roles;
    }

    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }
}
