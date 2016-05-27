package cfc.weinelzbeileh.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Comparator;

import cfc.weinelzbeileh.R;
import cfc.weinelzbeileh.classes.Information;

public class InformationActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Information> arrayAdapter;
    private DatabaseReference infoDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listView = (ListView) findViewById(R.id.listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Information info = arrayAdapter.getItem(position);
                String link = info.getLink();
                if (info.shouldOpenInApp()) {
                    Intent intent = new Intent(InformationActivity.this, InformationDisplayActivity.class);
                    intent.putExtra("Link", link);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        infoDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Information");
        createDatabaseConnection();
    }

    private void addInformation(DataSnapshot data) {
        new Information(data);
        refreshList();
    }

    private void createDatabaseConnection() {
        infoDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addInformation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Information.deleteInformation(dataSnapshot.getKey());
                addInformation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Information.deleteInformation(dataSnapshot.getKey());
                refreshList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void refreshList() {
        arrayAdapter.clear();
        arrayAdapter.addAll(Information.getInformationMap().values());
        arrayAdapter.sort(new Comparator<Information>() {
            @Override
            public int compare(Information lhs, Information rhs) {
                return lhs.getPriority() > rhs.getPriority() ? -1 : (lhs == rhs ? 1 : 0);
            }
        });
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Information.clear();
    }
}
