package cfc.weinelzbeileh.Static;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import cfc.weinelzbeileh.Classes.Information;

public class InformationManager {

    public static Map<String, Information> informationList = new HashMap<>();

    public static void init() {
        final Firebase information = new Firebase("https://wein-zbeileh-remake.firebaseio.com/Information");

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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
