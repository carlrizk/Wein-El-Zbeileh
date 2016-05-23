package cfc.weinelzbeileh.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import cfc.weinelzbeileh.Classes.Trash;
import cfc.weinelzbeileh.Classes.TrashType;
import cfc.weinelzbeileh.Main;
import cfc.weinelzbeileh.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap map;

    private int PERMISSIONS_REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ImageView informationButton = (ImageView) findViewById(R.id.information);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        for (TrashType t : TrashType.trashTypeMap.values()) {
            t.createButton(this);
        }

        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, InformationActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Main.database.goOnline();

        map.moveCamera(CameraUpdateFactory.newCameraPosition(Main.getLastCameraPosition()));

        if (!hasLocationPermission()) {
            requestLocationPermission();
        } else {
            enableMyLocation();
        }

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!isGPSEnabled()) {
                    buildAlertMessageNoGps(getResources().getString(R.string.gps_disabled), getResources().getString(R.string.yes), getResources().getString(R.string.no));
                }
                return false;
            }
        });

        Trash.createMarkers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (map != null) {
            Main.database.goOnline();
        }

        if (Main.isGPSWindowShowing) {
            buildAlertMessageNoGps(getResources().getString(R.string.gps_disabled), getResources().getString(R.string.yes), getResources().getString(R.string.no));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Main.database.goOffline();

        if (map != null) {
            Main.setLastCameraPosition(map.getCameraPosition());
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
                enableMyLocation();
            }
        }
    }

    private void buildAlertMessageNoGps(String text, String yesButton, String noButton) {
        Main.isGPSWindowShowing = true;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setCancelable(false)
                .setPositiveButton(yesButton, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        Main.isGPSWindowShowing = false;
                    }
                })
                .setNegativeButton(noButton, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        Main.isGPSWindowShowing = false;
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void enableMyLocation() {

        map.setMyLocationEnabled(true);

        if (!Main.alreadyRequestedGPS && !isGPSEnabled()) {
            buildAlertMessageNoGps(getResources().getString(R.string.gps_disabled), getResources().getString(R.string.yes), getResources().getString(R.string.no));
            Main.alreadyRequestedGPS = true;
        }

    }

    private boolean isGPSEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
