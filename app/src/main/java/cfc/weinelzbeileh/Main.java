package cfc.weinelzbeileh;

import android.app.Application;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cfc.weinelzbeileh.Classes.TrashType;
import cfc.weinelzbeileh.Static.InformationManager;

public class Main extends Application {

    public static DatabaseReference rootConnection;
    public static FirebaseDatabase database;


    public static boolean alreadyRequestedGPS = false;
    public static boolean isGPSWindowShowing = false;
    private static CameraPosition lastCameraPosition;

    public static CameraPosition getLastCameraPosition() {
        return lastCameraPosition;
    }

    public static void setLastCameraPosition(CameraPosition lastCameraPosition) {
        Main.lastCameraPosition = lastCameraPosition;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
        builder.setStorageBucket("wein-el-zbeileh-16b26.appspot.com");
        builder.setGcmSenderId("456969940708");
        builder.setApplicationId("wein-el-zbeileh-16b26");
        builder.setApiKey("AIzaSyD3g160Qt9r2kum36jPpOjW-qunku2ZOsc");
        builder.setDatabaseUrl("https://wein-el-zbeileh-16b26.firebaseio.com");
        FirebaseOptions options = builder.build();

        FirebaseApp app = FirebaseApp.initializeApp(getApplicationContext(), options, "Wein El Zbeileh");

        database = FirebaseDatabase.getInstance(app);
        rootConnection = database.getReference();

        database.goOffline();

        MapsInitializer.initialize(getApplicationContext());

        lastCameraPosition = new CameraPosition(new LatLng(33.8793113, 35.7611118), 8.5f, 0, 0);

        new TrashType("Metal", getApplicationContext().getString(R.string.trash_metal), R.drawable.metal);
        new TrashType("Paper", getApplicationContext().getString(R.string.trash_paper), R.drawable.paper);
        new TrashType("Glass", getApplicationContext().getString(R.string.trash_glass), R.drawable.glass);
        new TrashType("Plastic", getApplicationContext().getString(R.string.trash_plastic), R.drawable.plastic);

        InformationManager.init();
    }
}
