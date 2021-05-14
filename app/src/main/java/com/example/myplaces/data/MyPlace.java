package com.example.myplaces.data;

public class MyPlace {
    private String Name;
    private String Description;
    private String latitude;
    private String longitude;

    private int ID;

    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public MyPlace(String name, String description) {
        Name = name;
        Description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public MyPlace(String name) {
        this(name, "");
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return Name;
    }
}
