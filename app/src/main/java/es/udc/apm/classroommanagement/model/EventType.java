package es.udc.apm.classroommanagement.model;

/**
 * Created by Alejandro on 14/05/2017.
 */

public class EventType {
    private short id;
    private String name;

    public EventType(short id, String name) {
        this.id = id;
        this.name = name;
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
}
