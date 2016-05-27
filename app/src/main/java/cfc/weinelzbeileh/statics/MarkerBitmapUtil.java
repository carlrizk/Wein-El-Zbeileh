package cfc.weinelzbeileh.statics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfc.weinelzbeileh.classes.TrashType;

public class MarkerBitmapUtil {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final float DIVIDER = 0.75f;
    public static float MULTIPLIER;
    private static Map<List<TrashType>, Bitmap> bitmaps = new HashMap<>();

    public static Bitmap createBitmap(Context context, List<TrashType> trashTypes) {
        if (!bitmaps.containsKey(trashTypes)) {
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap((int) (trashTypes.size() * WIDTH * MULTIPLIER), (int) ((HEIGHT + 4) * MULTIPLIER), config);
            Canvas canvas = new Canvas(bmp);

            Paint color = new Paint();
            color.setColor(Color.WHITE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(0, 0, bmp.getWidth(), bmp.getHeight(), 10f * MULTIPLIER, 10f * MULTIPLIER, color);
            } else {
                canvas.drawRect(0, 0, bmp.getWidth(), bmp.getHeight(), color);
            }

            float x = 0;
            float y = 2 * MULTIPLIER;
            for (TrashType t : trashTypes) {
                canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), t.getIcon()), x, y, color);
                x += WIDTH * MULTIPLIER;
            }

            bitmaps.put(trashTypes, Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * DIVIDER), (int) (bmp.getHeight() * DIVIDER), true));
        }
        return bitmaps.get(trashTypes);
    }

}
