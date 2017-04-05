package es.udc.apm.classroommanagement.model;

/**
 * Created by danib on 20/03/2017.
 */

public class User {

    private short id;
    private String googleId;
    private String name;
    private String lastName;
    private String mail;
    private short roleId;

    public User() {
    }

    public User(short id, String googleId, String name, String lastName, String mail, short roleId) {
        this.id = id;
        this.googleId = googleId;
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
        this.roleId = roleId;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public short getRoleId() {
        return roleId;
    }

    public void setRoleId(short roleId) {
        this.roleId = roleId;
    }
}
