package cfc.weinelzbeileh.classes;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Information {

    private static Map<String, Information> informationMap = new HashMap<>();

    private DataSnapshot data;

    public Information(DataSnapshot data) {
        this.data = data;
        informationMap.put(data.getKey(), this);
    }

    public static void deleteInformation(String key) {
        informationMap.remove(key);
    }

    public static Map<String, Information> getInformationMap() {
        return informationMap;
    }

    public static void clear() {
        informationMap.clear();
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

    public boolean shouldOpenInApp() {
        if (data.child("In App").getValue() != null) {
            return (boolean) data.child("In App").getValue();
        }
        return false;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
