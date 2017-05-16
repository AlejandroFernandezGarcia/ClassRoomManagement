package es.udc.apm.classroommanagement.utils;

/**
 * Created by Alejandro on 16/05/2017.
 */

public class ScanningResultListDataModel {
    String hour;
    String days;
    String eventName;
    String eventType;
    String speaker;
    String eventDesc;

    public ScanningResultListDataModel(String hour, String days, String eventName, String eventType, String speaker, String eventDesc) {
        this.hour = hour;
        this.days = days;
        this.eventName = eventName;
        this.eventType = eventType;
        this.speaker = speaker;
        this.eventDesc = eventDesc;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }
}
