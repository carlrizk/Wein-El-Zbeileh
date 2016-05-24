package cfc.weinelzbeileh.Classes;

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
    private LatLng latlng;
    private Marker marker;

    public Trash(String id, double lat, double lng, List<TrashType> trashTypes) {
        this.latlng = new LatLng(lat, lng);
        this.trashTypes = trashTypes;
        for (TrashType t : trashTypes) {
            t.addTrash(this);
        }
        trashMap.put(id, this);

        createMarker();
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
            t.createMarker();
        }
    }

    public void createMarker() {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        this.marker = MapsActivity.map.addMarker(new MarkerOptions().position(latlng));
        updateMarker();
    }

    public void updateMarker() {
        if (marker != null) {

            List<TrashType> ts = new ArrayList<>();

            for (TrashType t : trashTypes) {
                if (t.isShowing()) {
                    ts.add(t);
                }
            }
            marker.setVisible(shouldShow());
            if (marker.isVisible()) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(MarkerBitmapUtil.createBitmap(ts)));
                marker.setAnchor(0.5f, 0.5f);
            }
        }
    }

    private boolean shouldShow() {
        for (TrashType t : trashTypes) {
            if (t.isShowing()) {
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
