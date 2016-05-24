package cfc.weinelzbeileh;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cfc.weinelzbeileh.Classes.TrashType;
import cfc.weinelzbeileh.Static.InformationManager;
import cfc.weinelzbeileh.Static.MarkerBitmapUtil;
import cfc.weinelzbeileh.Static.TrashConnection;

public class Main extends Application {

    public static DatabaseReference rootConnection;
    public static FirebaseDatabase database;
    public static Context context;

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

        context = getApplicationContext();

        MarkerBitmapUtil.MULTIPLIER = context.getResources().getDisplayMetrics().density;

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


        TrashType.assignColors(ContextCompat.getColor(context, R.color.white), ContextCompat.getColor(context, R.color.colorPrimary));


        new TrashType("Metal", R.drawable.metal);
        new TrashType("Paper", R.drawable.paper);
        new TrashType("Glass", R.drawable.glass);
        new TrashType("Plastic", R.drawable.plastic);

        InformationManager.init();

        TrashConnection.init();
    }
}
