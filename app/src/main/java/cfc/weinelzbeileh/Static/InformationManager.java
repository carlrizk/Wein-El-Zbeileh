package cfc.weinelzbeileh.Static;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import cfc.weinelzbeileh.Classes.Information;
import cfc.weinelzbeileh.Main;

public class InformationManager {

    private final static String folderAdress = "Information";
    public static Map<String, Information> informationList = new HashMap<>();

    public static void init() {
        final DatabaseReference information = Main.rootConnection.child(folderAdress);

        information.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                informationList.put(dataSnapshot.getKey(), new Information(dataSnapshot));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                informationList.remove(dataSnapshot.getKey());
                informationList.put(dataSnapshot.getKey(), new Information(dataSnapshot));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                informationList.remove(dataSnapshot.getKey());
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
