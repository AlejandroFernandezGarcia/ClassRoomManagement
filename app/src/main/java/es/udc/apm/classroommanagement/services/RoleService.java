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
    private static final RoleDAO roleDAO = new RoleDAO();

    public RoleService() {
    }

    @Override
    protected List<Role> doInBackground(Void... params) {
        return getAllRoles();
    }

    private List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }
}
