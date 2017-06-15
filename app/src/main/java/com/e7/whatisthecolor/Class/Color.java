package com.e7.whatisthecolor.Class;

/**
 * Created by Enrique on 10/06/2017.
 */

/** Color class */

public class Color {

    private String hex;
    private String name;

    private String red;
    private String green;
    private String blue;

    private String hue;
    private String saturation;
    private String lightness;

    /** Color class constructor */
    public Color (String hex, String name) {
        this.hex = hex;
        this.name = name;

        //Calc and save red, green, blue, hue, saturation and lightness
        //from the hexadecimal number of the color
        int r, g, b;
        int c = (int)Long.parseLong(hex, 16);
        r = (c >> 16) & 0xFF;
        g = (c >> 8) & 0xFF;
        b = (c >> 0) & 0xFF;

        int[] hsl = rgbToHsl(r,g,b);

        this.red            = r+"";
        this.green          = g+"";
        this.blue           = b+"";
        this.hue            = hsl[0]+"";
        this.saturation     = hsl[1]+"";
        this.lightness      = hsl[2]+"";
    }

    /**
     * Calc hue, saturation and lightness data from the red, green and
     * blue int values.
     *
     * @param  r    red int value
     * @param  g    green int value
     * @param  b    blue int value
     * @return      an array with hue, saturation and lightness data
     */
    public static int[] rgbToHsl(int r, int g, int b) {
        r /= 255;
        g /= 255;
        b /= 255;
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        int h, s, l;

        int delta = max - min;
        l = (min + max) / 2;

        s = 0;
        if(l > 0 && l < 1)
            s = delta / (l < 0.5 ? (2 * l) : (2 - 2 * l));

        h = 0;
        if(delta > 0)
        {
            if (max == r && max != g) h += (g - b) / delta;
            if (max == g && max != b) h += (2 + (b - r) / delta);
            if (max == b && max != r) h += (4 + (r - g) / delta);
            h /= 6;
        }

        int[] hsl = {h*255, s*255, l*255};
        return hsl;
    }


    /** Getters */

    public String getHex() {
        return this.hex;
    }

    public String getName() {
        return this.name;
    }

    public String getRed() {
        return this.red;
    }

    public String getGreen() {
        return this.green;
    }

    public String getBlue() {
        return this.blue;
    }

    public String getHue() {
        return this.hue;
    }

    public String getSaturation() {
        return this.saturation;
    }

    public String getLightness() {
        return this.lightness;
    }
}
