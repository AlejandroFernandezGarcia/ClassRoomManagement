package es.udc.apm.classroommanagement.model;

/**
 * Created by danib on 21/03/2017.
 */

public class Role {
    private short id;
    private String roleName;

    public Role() {
    }

    public Role(short id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
