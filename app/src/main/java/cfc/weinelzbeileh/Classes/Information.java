package cfc.weinelzbeileh.classes;

import java.util.HashMap;
import java.util.Map;

public class Information {

    private static Map<String, Integer> informationMap = new HashMap<>();

    private String title;
    private int layout;

    public Information(String title, int layout) {
        this.title = title;
        this.layout = layout;
        informationMap.put(title, layout);
    }

    public static Object[] getAll() {
        return informationMap.keySet().toArray();
    }

    public static int getLayout(String title) {
        return informationMap.get(title);
    }

    public String getTitle() {
        return title;
    }

    public int getLayout() {
        return layout;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
