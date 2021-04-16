package com.example.myplaces.data;

public class MyPlace {
    private String Name;
    private String Description;

    public MyPlace(String name, String description) {
        Name = name;
        Description = description;
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
