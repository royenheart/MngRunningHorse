package com.royenheart.mrh.universe;

/**
 * 国家对象
 *
 * @author RoyenHeart
 */
public class Country {

    // 国家数据

    private String name;
    private String code;

    public Country(String name, String code) {
        this.name = String.valueOf(name);
        this.code = String.valueOf(code);
    }

    public String getName() {
        return String.valueOf(this.name);
    }

    public String getCode() {
        return String.valueOf(this.code);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
