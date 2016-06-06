package cfc.weinelzbeileh.statics;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import cfc.weinelzbeileh.interfaces.FirebaseUtilInterface;

public class FirebaseUtil {

    private static final String TAG = "FirebaseUtil";
    private static Map<DatabaseReference, ChildEventListener> databaseMap = new HashMap<>();
    private static FirebaseDatabase database;

    public static void init(Context context) {
        if (database == null) {
            if (!FirebaseApp.getApps(context).isEmpty()) {
                database = FirebaseDatabase.getInstance();
                database.setPersistenceEnabled(true);
            } else {
                Log.e(TAG, "Not Initialised");
            }
        }
    }

    public static void addChild(String child, final FirebaseUtilInterface listener) {
        DatabaseReference reference = database.getReference().child(child);
        ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.OnAdd(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listener.OnRemove(dataSnapshot);
                listener.OnAdd(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listener.OnRemove(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseMap.put(reference, childListener);
    }

    public static void start() {
        for (DatabaseReference d : databaseMap.keySet()) {
            d.addChildEventListener(databaseMap.get(d));
        }
    }

    public static void pause() {
        for (DatabaseReference d : databaseMap.keySet()) {
            d.removeEventListener(databaseMap.get(d));
        }
    }

    public static void stop() {
        databaseMap.clear();
    }

}
