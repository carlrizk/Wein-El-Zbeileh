package cfc.weinelzbeileh.Static;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.HashMap;
import java.util.Map;

import cfc.weinelzbeileh.Classes.TrashType;
import cfc.weinelzbeileh.R;

public class TrashManager {

    private static Map<String, TrashType> trashTypeMap = new HashMap<>();

    public static void addTrashType(String trashID, TrashType trashType) {
        trashTypeMap.put(trashID, trashType);
    }

    public static void createMarkers() {
        for (TrashType t : trashTypeMap.values()) {
            t.createMarkers();
        }
    }

    public static void createButtons(Activity a) {
        TrashType.assignColors(a.getResources().getColor(R.color.white), a.getResources().getColor(R.color.colorPrimary));
        for (TrashType t : trashTypeMap.values()) {
            t.createButton(a);
        }
    }

    public static void updateMarkersOnCameraChange(LatLngBounds bounds) {
        for (TrashType t : trashTypeMap.values()) {
            t.updateMarkersOnCameraChange(bounds);
        }
    }
}
