package com.royenheart.mrh.universe;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 国家对象
 * <p>
 *     包含国家数据：
 *     名字，编号，所属行星
 * </p>
 *
 * @author RoyenHeart
 */
public class Country {

    // 国家约束

    public static int MAX_NAME_LENGTH = 12;
    public static int MIN_NAME_LENGTH = 1;

    // 国家数据

    @Expose
    private String name;
    @Expose
    private String code;

    /**
     * 卫星数据列表
     */
    @Expose()
    private final ArrayList<Satellite> sats;

    public Country(String name, String code, ArrayList<Satellite> sats) {
        this.name = String.valueOf(name);
        this.code = String.valueOf(code);
        this.sats = sats;
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
    public String toString() {
        return "" +
                String.format(
                        "国家名字:%s,国家编号:%s\n",
                        name, code
                );
    }

    public ArrayList<Satellite> getSats() {
        return sats;
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

    /**
     * 获取当前国家卫星数量
     *
     * @return 卫星数量
     */
    public int getAmountsSat() {
        return sats.size();
    }

}
