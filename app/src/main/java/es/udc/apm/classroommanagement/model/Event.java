package es.udc.apm.classroommanagement.model;

import java.util.List;

/**
 * Created by Alejandro on 14/05/2017.
 */

public class Event {

    private short id;
    private String name;
    private String speaker;
    private String description;
    private EventType type;
    private List<Schedule> scheduleList;

    public Event() {
    }

    public Event(short id, String name, String speaker, String description, EventType type, List<Schedule> scheduleList) {
        this.id = id;
        this.name = name;
        this.speaker = speaker;
        this.description = description;
        this.type = type;
        this.scheduleList = scheduleList;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
