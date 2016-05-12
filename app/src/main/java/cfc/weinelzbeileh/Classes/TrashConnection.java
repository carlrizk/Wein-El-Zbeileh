package cfc.weinelzbeileh.Classes;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import cfc.weinelzbeileh.Interfaces.OnTrash;

public class TrashConnection {

    private static String serverAdress = "https://wein-zbeileh-remake.firebaseio.com/Trash/";

    private String trashAdress;
    private Firebase connection;

    public TrashConnection(String trashID, final OnTrash listener) {
        this.trashAdress = serverAdress + trashID;
        this.connection = new Firebase(trashAdress);

        connection.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("Latitude").getValue() != null && dataSnapshot.child("Longitude").getValue() != null) {
                    listener.OnTrashAdded(dataSnapshot.getKey(), (double) dataSnapshot.child("Latitude").getValue(), (double) dataSnapshot.child("Longitude").getValue());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listener.OnTrashRemoved(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
