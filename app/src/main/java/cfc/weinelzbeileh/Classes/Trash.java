package cfc.weinelzbeileh.Classes;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfc.weinelzbeileh.Activities.MapsActivity;
import cfc.weinelzbeileh.Static.MarkerBitmapUtil;

public class Trash {

    public static Map<String, Trash> trashMap = new HashMap<>();

    private List<TrashType> trashTypes = new ArrayList<>();
    private Map<TrashType, Boolean> shouldShow = new HashMap<>();
    private LatLng latlng;
    private Marker marker;
    private Bitmap image;

    public Trash(String id, double lat, double lng, List<TrashType> trashTypes) {
        this.latlng = new LatLng(lat, lng);
        this.trashTypes = trashTypes;
        for (TrashType t : trashTypes) {
            t.addTrash(this);
            shouldShow.put(t, true);
        }
        this.image = MarkerBitmapUtil.createBitmap(trashTypes);
        trashMap.put(id, this);

        createMarker(shouldShow());
    }

    public static void removeTrash(String id) {
        Trash t = trashMap.get(id);
        if (t != null) {
            t.destroyMarker();
            for (TrashType trashtype : t.trashTypes) {
                trashtype.removeTrash(t);
            }
            trashMap.remove(id);
        }
    }

    public static void createMarkers() {
        for (Trash t : trashMap.values()) {
            t.createMarker(t.shouldShow());
        }
    }

    public void createMarker(boolean visible) {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        this.marker = MapsActivity.map.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromBitmap(image)));
        this.marker.setVisible(visible);
    }

    public void updateMarker() {
        if (marker != null) {
            marker.setVisible(shouldShow());
        }
    }

    public void updateVisibility(TrashType t, boolean showing) {
        if (shouldShow.containsKey(t)) {
            shouldShow.put(t, showing);
        }
        updateMarker();
    }

    private boolean shouldShow() {
        for (Boolean b : shouldShow.values()) {
            if (b == Boolean.TRUE) {
                return true;
            }
        }
        return false;
    }

    public void destroyMarker() {
        if (marker != null) {
            marker.remove();
        }
    }
}
