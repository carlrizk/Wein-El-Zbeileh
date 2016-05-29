package cfc.weinelzbeileh;

import android.app.Application;
import android.support.v4.content.ContextCompat;

import cfc.weinelzbeileh.classes.Information;
import cfc.weinelzbeileh.classes.TrashType;

public class Main extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TrashType.assignColors(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.colorPrimary));

        new TrashType("Glass", R.drawable.glass);
        new TrashType("Metal", R.drawable.metal);
        new TrashType("Plastic", R.drawable.plastic);
        new TrashType("Paper", R.drawable.paper);

        new Information(getString(R.string.information_title), R.layout.activity_maps);
    }
}
