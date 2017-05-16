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
    private String web_url;
    private String img_url;

    public Building(){}

    public Building(short id,double latitude, double longitude, String name, String phone, String address,
                    int zipCode, String region, String country, String web_url, String img_url) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
        this.region = region;
        this.country = country;
        this.web_url = web_url;
        this.img_url = img_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        if (getId() != building.getId()) return false;
        if (Double.compare(building.getLatitude(), getLatitude()) != 0) return false;
        if (Double.compare(building.getLongitude(), getLongitude()) != 0) return false;
        if (getZipCode() != building.getZipCode()) return false;
        if (getName() != null ? !getName().equals(building.getName()) : building.getName() != null)
            return false;
        if (getPhone() != null ? !getPhone().equals(building.getPhone()) : building.getPhone() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(building.getAddress()) : building.getAddress() != null)
            return false;
        if (getRegion() != null ? !getRegion().equals(building.getRegion()) : building.getRegion() != null)
            return false;
        if (getCountry() != null ? !getCountry().equals(building.getCountry()) : building.getCountry() != null)
            return false;
        if (getWeb_url() != null ? !getWeb_url().equals(building.getWeb_url()) : building.getWeb_url() != null)
            return false;
        return getImg_url() != null ? getImg_url().equals(building.getImg_url()) : building.getImg_url() == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) getId();
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + getZipCode();
        result = 31 * result + (getRegion() != null ? getRegion().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getWeb_url() != null ? getWeb_url().hashCode() : 0);
        result = 31 * result + (getImg_url() != null ? getImg_url().hashCode() : 0);
        return result;
    }

    public String getWeb_url() {

        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
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
        this.img_url = building.img_url;
        this.web_url = building.web_url;
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

        return address + ";" + phone + ";" + web_url + ";" + img_url;
    }

}
