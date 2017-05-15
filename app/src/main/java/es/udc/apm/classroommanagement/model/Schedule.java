package es.udc.apm.classroommanagement.model;

import java.sql.Time;

/**
 * Created by Alejandro on 14/05/2017.
 */

public class Schedule {
    private short id;
    private Time startHour;
    private Time endHour;

    public Schedule(){}

    public Schedule(short id, Time startHour, Time endHour) {
        this.id = id;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public Time getStartHour() {
        return startHour;
    }

    public void setStartHour(Time startHour) {
        this.startHour = startHour;
    }

    public Time getEndHour() {
        return endHour;
    }

    public void setEndHour(Time endHour) {
        this.endHour = endHour;
    }
}
