package com.example.myplaces.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlacesData {
    private ArrayList<MyPlace> MyPlaces;
    private HashMap<String, Integer> myPlacesKeyIndexMapping;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD = "my-places";
    private ListUpdatedEventListener updateListener;

    private MyPlacesData() {
        MyPlaces = new ArrayList<>();
        myPlacesKeyIndexMapping = new HashMap<>();
        database = FirebaseDatabase.getInstance("https://valiant-carrier-313719-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        database.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        database.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);
    }

    public void setEventListener(ListUpdatedEventListener listener) {
        updateListener = listener;
    }

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();
            if (!myPlacesKeyIndexMapping.containsKey(myPlaceKey)) {
                MyPlace myPlace = dataSnapshot.getValue(MyPlace.class);
                myPlace.key = myPlaceKey;
                MyPlaces.add(myPlace);
                myPlacesKeyIndexMapping.put(myPlaceKey, MyPlaces.size() - 1);
                if (updateListener != null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();
            MyPlace myPlace = dataSnapshot.getValue(MyPlace.class);
            myPlace.key = myPlaceKey;
            if (myPlacesKeyIndexMapping.containsKey(myPlaceKey)) {
                int index = myPlacesKeyIndexMapping.get(myPlaceKey);
                MyPlaces.set(index, myPlace);
            } else {
                MyPlaces.add(myPlace);
                myPlacesKeyIndexMapping.put(myPlaceKey, MyPlaces.size() - 1);
            }
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myPlacesKey = dataSnapshot.getKey();
            if (myPlacesKeyIndexMapping.containsKey(myPlacesKey)) {
                int index = myPlacesKeyIndexMapping.get(myPlacesKey);
                MyPlaces.remove(index);
                recreateKeyIndexMapping();
                if (updateListener != null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void addNewPlace(MyPlace place) {
        String key = database.push().getKey();
        MyPlaces.add(place);
        myPlacesKeyIndexMapping.put(key, MyPlaces.size() - 1);
        database.child(FIREBASE_CHILD).child(key).setValue(place);
        place.key = key;
    }

    public MyPlace getPlace(int index) {
        return MyPlaces.get(index);
    }

    public void deletePlace(int index) {
        database.child(FIREBASE_CHILD).child(MyPlaces.get(index).key).removeValue();
        MyPlaces.remove(index);
        recreateKeyIndexMapping();
    }

    private void recreateKeyIndexMapping() {
        myPlacesKeyIndexMapping.clear();
        for (int i = 0; i < MyPlaces.size(); i++) {
            myPlacesKeyIndexMapping.put(MyPlaces.get(i).key, i);
        }
    }

    public void updatePlace(int index, String name, String desc, String lng, String lat){
        MyPlace myPlace = MyPlaces.get(index);
        myPlace.Name = name;
        myPlace.Description = desc;
        myPlace.latitude = lat;
        myPlace.longitude = lng;
        database.child(FIREBASE_CHILD).child(myPlace.key).setValue(myPlace);
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

    public interface ListUpdatedEventListener {
        void onListUpdated();
    }


}

