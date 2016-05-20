package cfc.weinelzbeileh.Classes;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import cfc.weinelzbeileh.Activities.MapsActivity;

public class Trash {

    private TrashType parent;
    private LatLng latlng;
    private Marker marker;

    public Trash(TrashType parent, double lat, double lng) {
        this.parent = parent;
        this.latlng = new LatLng(lat, lng);
    }

    public void createMarker(String title, int icon, boolean visible) {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        this.marker = MapsActivity.map.addMarker(new MarkerOptions().title(title).position(latlng).icon(BitmapDescriptorFactory.fromResource(icon)));
        marker.setVisible(visible);
    }

    public void setVisible(boolean visible) {
        if (marker != null) {
            marker.setVisible(visible);
        }
    }

    public void destroyMarker() {
        if (marker != null) {
            marker.remove();
        }
    }

    public LatLng getLatlng() {
        return latlng;
    }
}
