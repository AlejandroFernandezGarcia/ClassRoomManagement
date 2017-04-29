package es.udc.apm.classroommanagement.services;

import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public List<Building> getAllBuildings() throws ExecutionException, InterruptedException {
        GetBuildingsTask getBuildingsTask = new GetBuildingsTask();
        List<Building> buildings = getBuildingsTask.execute().get();
        return buildings;
    }

    private class GetBuildingsTask extends AsyncTask<Void, Void, List<Building>> {

        @Override
        protected List<Building> doInBackground(Void... params) {
            if (buildingDAO == null) {
                buildingDAO = new BuildingDAO();
            }
            List<Building> result = buildingDAO.getAllBuildings();
            return result;
        }
    }

}
