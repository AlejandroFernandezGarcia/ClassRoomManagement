package es.udc.apm.classroommanagement.services;

import android.os.AsyncTask;
import android.os.Build;

import java.util.List;

import es.udc.apm.classroommanagement.dao.BuildingDAO;
import es.udc.apm.classroommanagement.model.Building;

/**
 * Created by Alejandro on 19/04/2017.
 */

public class BuildingService {
    //Constants
    private static final String TAG = BuildingService.class.getSimpleName();
    private BuildingDAO buildingDAO;

    public BuildingService() {
        buildingDAO = new BuildingDAO();
    }

    protected List<Building> doInBackground(Void... params) {
        if (this.buildingDAO == null) {
            this.buildingDAO = new BuildingDAO();
        }
        List<Building> result = buildingDAO.getAllBuildings();
        return result;
    }

    public List<Building> getAllBuildings() {
        return buildingDAO.getAllBuildings();
    }
}
