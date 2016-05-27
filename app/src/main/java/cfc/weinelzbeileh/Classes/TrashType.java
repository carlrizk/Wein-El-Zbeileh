package cfc.weinelzbeileh.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrashType {

    private static Map<String, TrashType> trashTypeMap = new HashMap<>();

    private List<Trash> includedTrashes;

    private String id;
    private int icon;
    private boolean showing = true;

    public TrashType(String id, int icon) {
        this.includedTrashes = new ArrayList<>();
        this.id = id;
        this.icon = icon;
        trashTypeMap.put(id, this);
    }

    public static void clear() {
        trashTypeMap.clear();
    }

    public static boolean exists(String key) {
        return trashTypeMap.containsKey(key);
    }

    public static TrashType get(String key) {
        return trashTypeMap.get(key);
    }

    public static Map<String, TrashType> getAll() {
        return trashTypeMap;
    }

    public int getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public List<Trash> getIncludedTrashes() {
        return includedTrashes;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public void toggleShowing() {
        showing = !showing;
    }

    public void insert(Trash trash) {
        includedTrashes.add(trash);
    }

    public void remove(Trash trash) {
        if (includedTrashes.contains(trash)) {
            includedTrashes.remove(trash);
        }
    }
}
