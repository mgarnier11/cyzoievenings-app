package com.mgarnier11.CyzoisEvenings.models;

import java.io.Serializable;

public class Type implements Serializable {
    public String value;

    public boolean group;

    public int id;

    public Type() {
        this.id = 0;
        this.value = "";
        this.group = false;
    }

}
