package cfc.weinelzbeileh.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class InformationDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = getIntent().getIntExtra("Layout", 99);
        if (layout == 99) {
            finish();
        } else {
            setContentView(layout);
        }
    }
}
