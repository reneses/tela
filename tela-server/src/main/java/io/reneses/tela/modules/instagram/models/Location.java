package io.reneses.tela.modules.instagram.models;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Location class.
 */
public class Location {

    private long id;
    private double latitude, longitude;
    private String name;

    @JsonProperty("street_address")
    private String streetAddress;

    /**
     * Getter for the field <code>id</code>.
     *
     * @return a long.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the field <code>id</code>.
     *
     * @param id a long.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the field <code>latitude</code>.
     *
     * @return a double.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter for the field <code>latitude</code>.
     *
     * @param latitude a double.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Getter for the field <code>longitude</code>.
     *
     * @return a double.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter for the field <code>longitude</code>.
     *
     * @param longitude a double.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Getter for the field <code>name</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the field <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the field <code>streetAddress</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Setter for the field <code>streetAddress</code>.
     *
     * @param streetAddress a {@link java.lang.String} object.
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (id != location.id) return false;
        if (Double.compare(location.latitude, latitude) != 0) return false;
        if (Double.compare(location.longitude, longitude) != 0) return false;
        if (name != null ? !name.equals(location.name) : location.name != null) return false;
        return streetAddress != null ? streetAddress.equals(location.streetAddress) : location.streetAddress == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (streetAddress != null ? streetAddress.hashCode() : 0);
        return result;
    }
}
