package com.e7.whatisthecolor.data.entity;

/**
 * Created by Enrique on 10/06/2017.
 */

import com.google.gson.annotations.SerializedName;

/** ColorEntity class */

public class ColorEntity {

    @SerializedName("hex") private String hex;
    @SerializedName("name") private String name;

    /** Getters */

    public String getHex() {
        return this.hex;
    }

    public String getName() {
        return this.name;
    }

    /** Setters */

    public void setHex(String hex) {
        this.hex = hex;
    }

    public void setName(String name) {
        this.name = name;
    }
}
