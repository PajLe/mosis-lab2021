package com.example.myplaces.data;

import java.util.ArrayList;

public class MyPlacesData {
    private ArrayList<MyPlace> MyPlaces;

    private MyPlacesData() {
        MyPlaces = new ArrayList<>();
//        MyPlaces.add(new MyPlace("Cair"));
//        MyPlaces.add(new MyPlace("Cerovo"));
//        MyPlaces.add(new MyPlace("Nis"));
//        MyPlaces.add(new MyPlace("Beograd"));
//        MyPlaces.add(new MyPlace("Aleksinac"));
    }

    public void addNewPlace(MyPlace place) {
        MyPlaces.add(place);
    }

    public MyPlace getPlace(int index) {
        return MyPlaces.get(index);
    }

    public void deletePlace(int index) {
        MyPlaces.remove(index);
    }

    public ArrayList<MyPlace> getMyPlaces() {
        return MyPlaces;
    }

    private static class SingletonHolder {
        public static final MyPlacesData instance = new MyPlacesData();
    }

    public static MyPlacesData getInstance() {
        return SingletonHolder.instance;
    }

}
