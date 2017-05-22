package es.udc.apm.classroommanagement.services;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import es.udc.apm.classroommanagement.dao.RoomDAO;
import es.udc.apm.classroommanagement.model.Room;

/**
 * Created by Alejandro on 14/05/2017.
 */

public class RoomService {

    //Constants
    private static final RoomDAO roomDAO = new RoomDAO();

    public RoomService() {
    }

    public Room getRoomInfo(short roomId) throws ExecutionException, InterruptedException {
        GetRoomInfoTask getRoomInfoTask = new GetRoomInfoTask();
        Room room = getRoomInfoTask.execute(roomId).get();
        getRoomInfoTask = null;
        return room;
    }

    private class GetRoomInfoTask extends AsyncTask<Short, Void, Room>{
        @Override
        protected Room doInBackground(Short... params) {
            short roomId = params[0];
            return roomDAO.getRoomInfo(roomId);
        }
    }
}
