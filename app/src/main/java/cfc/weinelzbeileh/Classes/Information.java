package cfc.weinelzbeileh.Classes;

import com.google.firebase.database.DataSnapshot;

import java.util.Locale;

public class Information {

    private DataSnapshot data;

    public Information(DataSnapshot data) {
        this.data = data;
    }

    public String getTitle() {
        String title = data.getKey();

        if (data.child(Locale.getDefault().getLanguage()).child("Title").getValue() != null) {
            title = data.child(Locale.getDefault().getLanguage()).child("Title").getValue().toString();
        }

        return title;
    }

    public String getLink() {
        String link = data.child("Link").getValue().toString();

        if (data.child(Locale.getDefault().getLanguage()).child("Link").getValue() != null) {
            link = data.child(Locale.getDefault().getLanguage()).child("Link").getValue().toString();
        }

        return link;
    }

    public long getPriority() {
        if (data.child("Priority").getValue() != null) {
            return (long) data.child("Priority").getValue();
        }
        return 0;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
