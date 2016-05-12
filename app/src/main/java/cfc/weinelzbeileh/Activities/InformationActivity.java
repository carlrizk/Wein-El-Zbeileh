package cfc.weinelzbeileh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Comparator;

import cfc.weinelzbeileh.Classes.Information;
import cfc.weinelzbeileh.R;
import cfc.weinelzbeileh.Static.InformationManager;

public class InformationActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Information> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listView = (ListView) findViewById(R.id.informationListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InformationActivity.this, InformationDisplayActivity.class);
                intent.putExtra("Link", arrayAdapter.getItem(position).getLink());
                intent.putExtra("Open In App", arrayAdapter.getItem(position).shouldOpenInApp());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayAdapter.clear();
        arrayAdapter.addAll(InformationManager.informationList.values());
        arrayAdapter.sort(new Comparator<Information>() {
            @Override
            public int compare(Information lhs, Information rhs) {
                return lhs.getPriority() > rhs.getPriority() ? -1 : (lhs == rhs ? 1 : 0);
            }
        });
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);
    }
}
