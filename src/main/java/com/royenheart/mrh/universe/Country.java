package com.royenheart.mrh.universe;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Country country = (Country) o;
        return Objects.equals(name, country.name) && Objects.equals(code, country.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

}
