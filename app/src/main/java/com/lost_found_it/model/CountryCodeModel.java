package com.lost_found_it.model;

import java.io.Serializable;

public class CountryCodeModel implements Serializable {
    private int flag;
    private String name;
    private String code;

    public CountryCodeModel(int flag, String name, String code) {
        this.flag = flag;
        this.name = name;
        this.code = code;
    }

    public int getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
