package com.example.myplaces.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MyPlace {
    public String Name;
    public String Description;
    public String latitude;
    public String longitude;

    @Exclude
    public String key;

    public MyPlace() {
    }

    public MyPlace(String name, String desc) {
        this.Name = name;
        this.Description = desc;
    }

    public MyPlace(String name) {
        this(name, "");
    }

    @Override
    public String toString() {
        return Name;
    }
}
