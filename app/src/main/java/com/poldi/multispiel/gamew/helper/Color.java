package com.poldi.multispiel.gamew.helper;

import java.util.HashMap;

public class Color {

    private static final HashMap<String, Integer> colors = new HashMap<>();

    public static void populateColors() {
        colors.put("BLACK", android.graphics.Color.BLACK);
        colors.put("YELLOW", android.graphics.Color.YELLOW);
        colors.put("BLUE", android.graphics.Color.BLUE);
        colors.put("MAGENTA", android.graphics.Color.MAGENTA);
        colors.put("RED", android.graphics.Color.RED);
        colors.put("CYAN", android.graphics.Color.CYAN);
        colors.put("DARK_GRAY", android.graphics.Color.DKGRAY);
        colors.put("GRAY", android.graphics.Color.GRAY);
        colors.put("LIGHT_GRAY", android.graphics.Color.LTGRAY);
        colors.put("GREEN", android.graphics.Color.GREEN);
        colors.put("WHITE", android.graphics.Color.WHITE);
        colors.put("LIGHT_YELLOW", android.graphics.Color.rgb(255, 255, 179) );
        colors.put("LIGHT_BLUE", android.graphics.Color.rgb(179, 179, 255) );
        colors.put("LIGHT_MAGENTA", android.graphics.Color.rgb(255, 179, 255) );
        colors.put("LIGHT_RED", android.graphics.Color.rgb(255, 179, 179) );
        colors.put("LIGHT_CYAN", android.graphics.Color.rgb(179, 255, 255) );
        colors.put("LIGHT_GREEN", android.graphics.Color.rgb(179, 255, 179) );
        colors.put("LIME", android.graphics.Color.rgb(204, 255, 51) );
        colors.put("LIGHT_LIME", android.graphics.Color.rgb(236, 255, 17) );
        colors.put("ORANGE", android.graphics.Color.rgb(255, 153, 51) );
        colors.put("LIGHT_ORANGE", android.graphics.Color.rgb(255, 217, 179) );
        colors.put("BROWN", android.graphics.Color.rgb(102, 51, 0) );
        colors.put("DARK_BROWN", android.graphics.Color.rgb(77, 38, 0) );
    }

}
