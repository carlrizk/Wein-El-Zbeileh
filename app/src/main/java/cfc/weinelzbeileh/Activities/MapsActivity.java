package cfc.weinelzbeileh.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfc.weinelzbeileh.R;
import cfc.weinelzbeileh.classes.Trash;
import cfc.weinelzbeileh.classes.TrashType;
import cfc.weinelzbeileh.interfaces.FirebaseUtilInterface;
import cfc.weinelzbeileh.statics.FirebaseUtil;
import cfc.weinelzbeileh.statics.MarkerBitmapUtil;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String CAMERA_LATITUDE = "CAMERA_LATITUDE";
    private static final String CAMERA_LONGITUDE = "CAMERA_LONGITUDE";
    private static final String CAMERA_ZOOM = "CAMERA_ZOOM";

    private int PERMISSIONS_REQUEST_LOCATION = 0;
    private Map<TrashType, ImageView> toggleButtons = new HashMap<>();
    private GoogleMap map;

    //Data To Load \\
    private CameraPosition lastCameraPosition;

    private void LoadData(Bundle bundle) {
        if (bundle != null) {
            double lat = bundle.getDouble(CAMERA_LATITUDE);
            double lng = bundle.getDouble(CAMERA_LONGITUDE);
            float zoom = bundle.getFloat(CAMERA_ZOOM);
            lastCameraPosition = new CameraPosition(new LatLng(lat, lng), zoom, 0, 0);
        } else {
            lastCameraPosition = new CameraPosition(new LatLng(33.8793113, 35.7611118), 8.5f, 0, 0);
        }
        MarkerBitmapUtil.MULTIPLIER = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createInformationButton();

        LoadData(savedInstanceState);

        FirebaseUtil.init(this);

        createToggleButtons();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUtil.addChild("Trash", new FirebaseUtilInterface() {
            @Override
            public void OnAdd(DataSnapshot data) {
                addTrash(data);
            }

            @Override
            public void OnRemove(DataSnapshot data) {
                Trash.get(data.getKey()).destroy();
            }
        });
    }

    private void addTrash(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.getKey();
        DataSnapshot position = dataSnapshot.child("Position");
        LatLng latlng = new LatLng((double) position.child("Latitude").getValue(), (double) position.child("Longitude").getValue());
        DataSnapshot type = dataSnapshot.child("Type");
        List<TrashType> trashTypes = new ArrayList<>();
        for (DataSnapshot data : type.getChildren()) {
            if (TrashType.exists(data.getKey())) {
                if ((boolean) data.getValue()) {
                    trashTypes.add(TrashType.get(data.getKey()));
                }
            }
        }
        Trash t = new Trash(id, latlng, trashTypes);
        if (map != null) {
            t.createMarker(this, map);
        }
    }

    private void createInformationButton() {
        findViewById(R.id.information).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, InformationActivity.class));
            }
        });
    }

    private void createToggleButtons() {
        for (TrashType t : TrashType.getAll().values()) {
            final TrashType trashType = t;
            LinearLayout layout = (LinearLayout) findViewById(R.id.toggleLinearLayout);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            params.setMargins(2, 0, 2, 0);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trashType.toggleShowing();
                    updateToggleButton(trashType);
                    for (Trash t : trashType.getIncludedTrashes()) {
                        t.updateMarker(getApplicationContext());
                    }
                }
            };

            ImageView button = new ImageView(this);
            button.setLayoutParams(params);
            button.setImageResource(t.getIcon());
            button.setPadding(0, 4, 0, 4);
            button.setOnClickListener(listener);

            layout.addView(button);
            if (toggleButtons.containsKey(trashType)) {
                toggleButtons.remove(trashType);
            }
            toggleButtons.put(trashType, button);

            updateToggleButton(trashType);
        }
    }

    private void updateToggleButton(TrashType trashType) {
        ImageView button = toggleButtons.get(trashType);
        int color;
        if (trashType.isShowing()) {
            color = TrashType.getColorEnabled();
        } else {
            color = TrashType.getColorDisabled();
        }
        button.setBackgroundColor(color);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        FirebaseUtil.start();

        map.moveCamera(CameraUpdateFactory.newCameraPosition(lastCameraPosition));

        if (!hasLocationPermission()) {
            requestLocationPermission();
        } else {
            map.setMyLocationEnabled(true);
        }

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!isGPSEnabled()) {
                    Toast.makeText(getApplicationContext(), R.string.gps_disabled, Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        for (Trash t : Trash.getAll().values()) {
            t.createMarker(this, map);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (map != null) {
            FirebaseUtil.start();
        }
    }

    private boolean hasLocationPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        }
    }

    private boolean isGPSEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save Data \\
        outState.putDouble(CAMERA_LATITUDE, lastCameraPosition.target.latitude);
        outState.putDouble(CAMERA_LONGITUDE, lastCameraPosition.target.longitude);
        outState.putFloat(CAMERA_ZOOM, lastCameraPosition.zoom);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onPause() {
        if (map != null) {
            lastCameraPosition = map.getCameraPosition();
        }

        FirebaseUtil.pause();

        super.onPause();
    }

    @Override
    protected void onStop() {
        FirebaseUtil.stop();
        Trash.clear();

        super.onStop();
    }
}
