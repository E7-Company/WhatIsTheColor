package com.e7.whatisthecolor.view.viewmodel;

/**
 * Created by Enrique on 10/06/2017.
 */

/** ColorViewModel class */

public class ColorViewModel {

    private String hex;
    private String name;

    private String red;
    private String green;
    private String blue;

    private String hue;
    private String saturation;
    private String lightness;


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


    /** Setters */

    public void setHex(String hex) {
        this.hex = hex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public void setGreen(String green) {
        this.green = green;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public void setHue(String hue) {
        this.hue = hue;
    }

    public void setSaturation(String saturation) {
        this.saturation = saturation;
    }

    public void setLightness(String lightness) {
        this.lightness = lightness;
    }
}

