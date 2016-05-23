package cfc.weinelzbeileh.Classes;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfc.weinelzbeileh.R;

public class TrashType {

    public static Map<String, TrashType> trashTypeMap = new HashMap<>();
    private static int enabledColor, disabledColor;
    private List<Trash> trashList = new ArrayList<>();
    private ImageView button;

    private int icon;
    private boolean showing = true;

    public TrashType(String trashType, int icon) {

        this.icon = icon;

        trashTypeMap.put(trashType, this);
    }

    public static void assignColors(int enabled, int disabled) {
        enabledColor = enabled;
        disabledColor = disabled;
    }

    public void addTrash(Trash t) {
        trashList.add(t);
    }

    public void removeTrash(Trash t) {
        trashList.remove(t);
    }

    public void toggleShowing() {
        showing = !showing;
        for (Trash t : trashList) {
            t.updateVisibility(this, showing);
        }
        updateButton();
    }

    public void createButton(Activity a) {
        LinearLayout layout = (LinearLayout) a.findViewById(R.id.toggleLinearLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(2, 0, 2, 0);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowing();
            }
        };

        if (button != null) {
            layout.removeView(button);
        }

        button = new ImageView(a);
        button.setLayoutParams(params);
        button.setImageResource(icon);
        button.setPadding(0, 4, 0, 4);
        button.setOnClickListener(listener);

        layout.addView(button);

        updateButton();
    }

    public void updateButton() {
        if (button != null) {
            if (showing) {
                button.setBackgroundColor(enabledColor);
            } else {
                button.setBackgroundColor(disabledColor);
            }
        }
    }

    public int getIcon() {
        return icon;
    }
}
