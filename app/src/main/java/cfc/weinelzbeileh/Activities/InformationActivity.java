package cfc.weinelzbeileh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cfc.weinelzbeileh.R;
import cfc.weinelzbeileh.classes.Information;

public class InformationActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Object> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        listView = (ListView) findViewById(R.id.listview);
    }

    @Override
    protected void onStart() {
        super.onStart();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Information.getAll());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = (String) arrayAdapter.getItem(position);
                int layout = Information.getLayout(title);
                Intent intent = new Intent(InformationActivity.this, InformationDisplayActivity.class);
                intent.putExtra("Layout", layout);
                startActivity(intent);
            }
        });

        listView.setAdapter(arrayAdapter);
    }
}
