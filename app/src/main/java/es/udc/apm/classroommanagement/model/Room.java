package es.udc.apm.classroommanagement.model;

import java.util.List;

/**
 * Created by Alejandro on 14/05/2017.
 */

public class Room {

    private short id;
    private String name;
    private List<Event> events;
    private Building building;

    public Room() {
    }

    public Room(short id, String name, List<Event> events, Building building) {
        this.id = id;
        this.name = name;
        this.events = events;
        this.building = building;
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
