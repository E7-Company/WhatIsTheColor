package com.e7.whatisthecolor.Functions;

import android.content.Context;

import com.e7.whatisthecolor.Classes.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Enrique on 28/02/2017.
 */

/** Colors class to get data from the default colors */
public class Colors {

    private static Color[] colors;

    /**
     * Load all colors data from a JSON file and save it in an array.
     *
     * @param  context   the main context from the app
     * @param  filename  file name to get data
     */

    public static void loadColors(Context context, String filename) {
        if(colors == null) {
            String json = null;
            //Read the JSON file
            try {
                InputStream is = context.getAssets().open(filename);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            parseColors(json);
        }
    }

    private static void parseColors(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("Colors");
            colors = new Color[m_jArry.length()];
            //Get the JSON data and save it in a Color object
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Color color = new Color(jo_inside.getString("hex"), jo_inside.getString("name"));
                colors[i] = color;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return all colors saved in the array from file.
     *
     * @return           array of colors
     */
    public static Color[] getColors() {
        return colors;
    }

    /**
     * Returns a String with the name of the color more approximated
     * from the colors array loaded.
     * <p>
     * This method calc the RGB and HSL from the hexadecimal String
     * and compare it with all the list from colors.
     *
     * @param  color the hexadecimal number from a color
     * @return       a color name
     */
    public static String getColorName(String color) {
        int c = (int)Long.parseLong(color, 16);
        int r = (c >> 16) & 0xFF;
        int g = (c >> 8) & 0xFF;
        int b = (c >> 0) & 0xFF;

        int []hsl = Color.rgbToHsl(r,g,b);
        int h = hsl[0], s = hsl[1], l = hsl[2];

        int ndf1;
        int ndf2;
        int ndf;
        int cl = -1, df = -1;

        for(int i = 0; i < colors.length; i++)
        {
            if(color == "#" + colors[i].getHex())
                return colors[i].getName();

            ndf1 = (int) (Math.pow(r - Integer.parseInt(colors[i].getRed()), 2) + Math.pow(g - Integer.parseInt(colors[i].getGreen()), 2) + Math.pow(b - Integer.parseInt(colors[i].getBlue()), 2));
            ndf2 = (int) (Math.pow(h - Integer.parseInt(colors[i].getHue()), 2) + Math.pow(s - Integer.parseInt(colors[i].getSaturation()), 2) + Math.pow(l - Integer.parseInt(colors[i].getLightness()), 2));
            ndf = ndf1 + ndf2 * 2;
            if(df < 0 || df > ndf)
            {
                df = ndf;
                cl = i;
            }
        }

        return colors[cl].getName();
    }
}
