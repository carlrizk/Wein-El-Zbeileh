package cfc.weinelzbeileh.Classes;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import cfc.weinelzbeileh.Interfaces.OnTrash;
import cfc.weinelzbeileh.Main;

public class TrashConnection {

    private final static String folderAdress = "Trash";

    private DatabaseReference connection;

    public TrashConnection(String trashID, final OnTrash listener) {
        this.connection = Main.rootConnection.child(folderAdress).child(trashID);

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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
