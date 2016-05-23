package cfc.weinelzbeileh.Static;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import cfc.weinelzbeileh.Classes.Trash;
import cfc.weinelzbeileh.Classes.TrashType;
import cfc.weinelzbeileh.Main;

public class TrashConnection {

    private static final String folder = "Trash";

    public static void init() {
        DatabaseReference ref = Main.rootConnection.child(folder);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                deleteMarker(dataSnapshot);
                addMarker(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                deleteMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static void addMarker(DataSnapshot snap) {
        double lat = (double) snap.child("Position").child("Latitude").getValue();
        double lng = (double) snap.child("Position").child("Longitude").getValue();

        List<TrashType> trashTypes = new ArrayList<>();

        for (DataSnapshot d : snap.child("Type").getChildren()) {
            if (TrashType.trashTypeMap.containsKey(d.getKey())) {
                if ((boolean) d.getValue()) {
                    trashTypes.add(TrashType.trashTypeMap.get(d.getKey()));
                }
            }
        }

        new Trash(snap.getKey(), lat, lng, trashTypes);
    }

    private static void deleteMarker(DataSnapshot snap) {
        if (Trash.trashMap.containsKey(snap.getKey())) {
            Trash.removeTrash(snap.getKey());
        }
    }

}
