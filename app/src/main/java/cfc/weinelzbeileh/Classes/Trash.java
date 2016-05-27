package cfc.weinelzbeileh.classes;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfc.weinelzbeileh.statics.MarkerBitmapUtil;

public class Trash {

    private static Map<String, Trash> trashMap = new HashMap<>();

    private String id;
    private LatLng position;
    private List<TrashType> trashTypes;
    private Marker marker;

    public Trash(String id, LatLng position, List<TrashType> trashTypes) {
        this.id = id;
        this.position = position;
        this.trashTypes = trashTypes;
        for (TrashType t : trashTypes) {
            t.insert(this);
        }
        trashMap.put(id, this);
    }

    public static void clear() {
        trashMap.clear();
    }

    public static Trash get(String id) {
        return trashMap.get(id);
    }

    public static Map<String, Trash> getAll() {
        return trashMap;
    }

    public void destroy() {
        if (marker != null) {
            marker.remove();
        }
        trashMap.remove(this);
        for (TrashType t : trashTypes) {
            t.remove(this);
        }
    }

    public void createMarker(Context context, GoogleMap map) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(position));
        updateMarker(context);
    }

    public void updateMarker(Context context) {
        if (marker != null) {

            List<TrashType> ts = new ArrayList<>();

            for (TrashType t : trashTypes) {
                if (t.isShowing()) {
                    ts.add(t);
                }
            }
            marker.setVisible(shouldShow());
            if (marker.isVisible()) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(MarkerBitmapUtil.createBitmap(context, ts)));
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

    public String getId() {
        return id;
    }

    public LatLng getPosition() {
        return position;
    }

    public List<TrashType> getTrashTypes() {
        return trashTypes;
    }

}
