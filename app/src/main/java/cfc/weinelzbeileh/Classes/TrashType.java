package cfc.weinelzbeileh.Classes;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.HashMap;
import java.util.Map;

import cfc.weinelzbeileh.Activities.MapsActivity;
import cfc.weinelzbeileh.Interfaces.OnTrash;
import cfc.weinelzbeileh.Main;
import cfc.weinelzbeileh.R;
import cfc.weinelzbeileh.Static.TrashManager;

public class TrashType {

    private static int enabledColor, disabledColor;
    private Map<String, Trash> trashMap = new HashMap<>();
    private String trashType;
    private TrashConnection connection;

    private ImageView button;

    private String trashTitle;

    private int icon;

    private boolean showing = true;

    public TrashType(String trashtype, String trashTitle, int icon) {
        this.trashType = trashtype;
        this.trashTitle = trashTitle;
        this.icon = icon;

        this.connection = new TrashConnection(trashtype, new OnTrash() {

            @Override
            public void OnTrashAdded(String id, double latitude, double longitude) {
                createTrash(id, latitude, longitude);
            }

            @Override
            public void OnTrashRemoved(String id) {
                deleteTrash(id);
            }
        });

        TrashManager.addTrashType(trashType, this);
    }

    public static void assignColors(int enabled, int disabled) {
        enabledColor = enabled;
        disabledColor = disabled;
    }

    public void updateMarkersOnCameraChange(LatLngBounds bounds) {
        for (Trash t : trashMap.values()) {
            if (bounds.contains(t.getLatlng())) {
                t.setVisible(showing);
            } else {
                t.setVisible(false);
            }
        }
    }

    private void createTrash(String id, double lat, double lng) {
        Trash trash = new Trash(this, lat, lng);
        trashMap.put(id, trash);
        if (Main.isAppInForeground && MapsActivity.map != null) {
            trash.createMarker(MapsActivity.map, trashTitle, icon, showing);
        }
    }

    private void deleteTrash(String id) {
        if (trashMap.containsKey(id)) {
            Trash t = trashMap.get(id);
            trashMap.remove(id);
            t.destroyMarker();
        }
    }

    public void createMarkers() {
        for (Trash t : trashMap.values()) {
            t.createMarker(MapsActivity.map, trashTitle, icon, showing);
        }
    }

    public void toggleShowing() {
        showing = !showing;
        for (Trash t : trashMap.values()) {
            t.setVisible(showing);
        }
        updateMarkersOnCameraChange(MapsActivity.map.getProjection().getVisibleRegion().latLngBounds);
        updateButton();
    }

    public void createButton(Activity a) {
        LinearLayout layout = (LinearLayout) a.findViewById(R.id.toggleLinearLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(2, 0, 2, 0);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowing();
            }
        };

        if (button != null) {
            layout.removeView(button);
        }

        button = new ImageView(a);
        button.setLayoutParams(params);
        button.setImageResource(icon);
        button.setPadding(0, 0, 0, 0);
        button.setOnClickListener(listener);

        layout.addView(button);

        updateButton();
    }

    public void updateButton() {
        if (button != null) {
            if (showing) {
                button.setBackgroundColor(enabledColor);
            } else {
                button.setBackgroundColor(disabledColor);
            }
        }
    }
}
