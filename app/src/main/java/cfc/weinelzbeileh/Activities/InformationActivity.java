package cfc.weinelzbeileh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Comparator;

import cfc.weinelzbeileh.R;
import cfc.weinelzbeileh.classes.Information;

public class InformationActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Information> arrayAdapter;

    private AdapterView.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(R.string.loading);
        }

        listView = (ListView) findViewById(R.id.listview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Information info = arrayAdapter.getItem(position);
                String link = info.getLink();
                //if (info.shouldOpenInApp()) {
                Intent intent = new Intent(InformationActivity.this, InformationDisplayActivity.class);
                intent.putExtra("Link", link);
                startActivity(intent);
                //} else {
                //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                //}
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();

        listView.setOnItemClickListener(onItemClickListener);
    }

    private void refreshList() {
        arrayAdapter.addAll(Information.getInformationMap().values());
        arrayAdapter.sort(new Comparator<Information>() {
            @Override
            public int compare(Information lhs, Information rhs) {
                return lhs.getPriority() > rhs.getPriority() ? -1 : (lhs == rhs ? 1 : 0);
            }
        });
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);
        if (arrayAdapter.getCount() > 0 && getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        arrayAdapter.clear();
        listView.setOnItemClickListener(null);
        onItemClickListener = null;
    }
}
