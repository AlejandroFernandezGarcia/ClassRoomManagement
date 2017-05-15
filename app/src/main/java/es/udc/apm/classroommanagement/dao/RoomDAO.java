package es.udc.apm.classroommanagement.dao;


import java.sql.SQLException;
import java.util.ArrayList;

import es.udc.apm.classroommanagement.model.Building;
import es.udc.apm.classroommanagement.model.Event;
import es.udc.apm.classroommanagement.model.EventType;
import es.udc.apm.classroommanagement.model.Room;
import es.udc.apm.classroommanagement.model.Schedule;

import static es.udc.apm.classroommanagement.utils.Utils.logError;

/**
 * Created by Alejandro on 14/05/2017.
 */

public class RoomDAO {

    //Constants
    private static final String TAG = UserDAO.class.getSimpleName();
    private final ConnectionManager connection;

    public RoomDAO() {
        this.connection = new ConnectionManager();
    }

    public Room getRoomInfo(short roomId) {
        String query = ("SELECT r.ROOM_ID,b.BUILD_NAME, r.ROOM_NAME, e.EVENT_NAME, e.EVENT_SPEAKER," +
                "et.ETYPE_NAME, sc.SCHEDULE_START_HOUR, sc.SCHEDULE_END_HOUR " +
                "FROM TROOM r " +
                "INNER JOIN TBUILDING b ON r.BUILD_ID = b.BUILD_ID " +
                "INNER JOIN TEVENT e ON e.ROOM_ID = r.ROOM_ID " +
                "INNER JOIN TEVENT_TYPE et ON et.ETYPE_ID = e.ETYPE_ID " +
                "INNER JOIN TSCHEDULE sc ON sc.EVENT_ID = e.EVENT_ID " +
                "WHERE r.ROOM_ID=") + roomId;
        Room room = new Room();
        room.setBuilding(new Building());
        room.setEvents(new ArrayList<Event>());
        try {
            connection.executeQuery(query);
            while (connection.getResult().next()) {
                room.setId(connection.getResult().getShort("r.ROOM_ID"));
                room.setName(connection.getResult().getString("r.ROOM_NAME"));
                room.getBuilding().setName(connection.getResult().getString("b.BUILD_NAME"));
                Event event = new Event();
                event.setName(connection.getResult().getString("e.EVENT_NAME"));
                event.setSpeaker(connection.getResult().getString("e.EVENT_SPEAKER"));
                event.setType(new EventType((short) 1, connection.getResult().getString("et.ETYPE_NAME")));
                event.setScheduleList(new ArrayList<Schedule>());

                Schedule schedule = new Schedule();
                schedule.setStartHour(connection.getResult().getTime("sc.SCHEDULE_START_HOUR"));
                schedule.setEndHour(connection.getResult().getTime("sc.SCHEDULE_END_HOUR"));
                event.getScheduleList().add(schedule);
                room.getEvents().add(event);
            }
        } catch (SQLException e) {
            logError(this, e);
        }
        return room;
    }
}
