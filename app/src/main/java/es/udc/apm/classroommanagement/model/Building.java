package es.udc.apm.classroommanagement.model;

/**
 * Created by david on 12/03/17.
 */

public class Building {

    private short id;
    private double latitude;
    private double longitude;
    private String name;
    private String phone;
    private String address;
    private int zipCode;
    private String region;
    private String country;

    public Building(short id,double latitude, double longitude, String name, String phone, String address,
                    int zipCode, String region, String country) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
        this.region = region;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;

        Building that = (Building) o;

        if (Double.compare(that.getLatitude(), getLatitude()) != 0) return false;
        if (Double.compare(that.getLongitude(), getLongitude()) != 0) return false;
        if (getZipCode() != that.getZipCode()) return false;
        if (!getName().equals(that.getName())) return false;
        if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(that.getAddress()) : that.getAddress() != null)
            return false;
        return getRegion() != null ? getRegion().equals(that.getRegion()) : that.getRegion() == null && (getCountry() != null ? getCountry().equals(that.getCountry()) : that.getCountry() == null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLatitude());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + getZipCode();
        result = 31 * result + (getRegion() != null ? getRegion().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        return result;
    }

    public int getZipCode() {

        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Building(Building building) {
        this.latitude = building.latitude;
        this.longitude = building.longitude;
        this.name = building.name;
        this.phone = building.phone;
        this.address = building.address;
        this.zipCode = building.zipCode;
        this.region = building.region;
        this.country = building.country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfoString(){

        return "Dirección: " + address + "\n" + "Teléfono:" + phone + "\n";
    }

}
