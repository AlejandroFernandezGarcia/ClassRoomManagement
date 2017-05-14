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
    private static final BuildingDAO buildingDAO = new BuildingDAO();

    public BuildingService() {
    }

    public List<Building> getAllBuildings() throws ExecutionException, InterruptedException {
        GetBuildingsTask getBuildingsTask = new GetBuildingsTask();
        return getBuildingsTask.execute().get();
    }

    private class GetBuildingsTask extends AsyncTask<Void, Void, List<Building>> {

        @Override
        protected List<Building> doInBackground(Void... params) {
            return buildingDAO.getAllBuildings();
        }
    }

}
