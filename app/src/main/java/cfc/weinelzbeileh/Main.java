package cfc.weinelzbeileh;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import cfc.weinelzbeileh.Classes.TrashType;
import cfc.weinelzbeileh.Static.InformationManager;

public class Main extends Application {

    public static boolean alreadyRequestedGPS = false;
    public static boolean isGPSWindowShowing = false;
    public static boolean isAppInForeground = false;
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

        Firebase.setAndroidContext(getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        MapsInitializer.initialize(getApplicationContext());

        lastCameraPosition = new CameraPosition(new LatLng(33.8793113, 35.7611118), 8.5f, 0, 0);

        new TrashType("Metal", getApplicationContext().getString(R.string.trash_metal), R.drawable.metal);
        new TrashType("Paper", getApplicationContext().getString(R.string.trash_paper), R.drawable.paper);
        new TrashType("Glass", getApplicationContext().getString(R.string.trash_glass), R.drawable.glass);
        new TrashType("Plastic", getApplicationContext().getString(R.string.trash_plastic), R.drawable.plastic);

        InformationManager.init();
    }
}
