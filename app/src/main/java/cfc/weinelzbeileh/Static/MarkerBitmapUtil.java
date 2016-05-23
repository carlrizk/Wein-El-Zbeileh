package cfc.weinelzbeileh.Static;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import java.util.List;

import cfc.weinelzbeileh.Classes.TrashType;
import cfc.weinelzbeileh.Main;

public class MarkerBitmapUtil {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final int MULTIPLIER = 3;
    private static final float DIVIDER = 0.75f;

    public static Bitmap createBitmap(List<TrashType> trashTypes) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap((4 + trashTypes.size() * WIDTH) * MULTIPLIER, (HEIGHT + 4) * MULTIPLIER, config);
        Canvas canvas = new Canvas(bmp);

        Paint color = new Paint();
        color.setColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0, bmp.getWidth(), bmp.getHeight(), 30f, 30f, color);
        } else {
            canvas.drawRect(0, 0, bmp.getWidth(), bmp.getHeight(), color);
        }

        int x = 2 * MULTIPLIER;
        int y = 2 * MULTIPLIER;
        for (TrashType t : trashTypes) {
            canvas.drawBitmap(BitmapFactory.decodeResource(Main.context.getResources(), t.getIcon()), x, y, color);
            x += WIDTH * MULTIPLIER;
        }

        return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * DIVIDER), (int) (bmp.getHeight() * DIVIDER), true);
    }

}
