package com.royenheart.mrh.universe;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 行星对象
 * <p>
 *     包括行星数据：
 *     名字、背景故事、行星大小
 * </p>
 *
 * @author RoyenHeart
 */
public class Planet {

    // 行星约束

    public static final int MIN_SIZE = 64;
    public static final int MAX_SIZE = 1024;

    // 行星数据

    private String name;
    private String desc;
    private int size;

    /** 国家数据，行星生成后录入 */
    public ArrayList<Country> ctys = new ArrayList<>();

    public Planet(String name, String desc, int size) {
        this.name = name;
        this.desc = desc;
        this.size = size;
    }

    /**
     * 判断行星是否相同
     *
     * @param o 判断的对象
     * @return 是否相同
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Planet planet = (Planet) o;
        return size == planet.size && Objects.equals(name, planet.name) && Objects.equals(desc, planet.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, desc, size);
    }

    public String getName() {
        return String.valueOf(name);
    }

    public int getSize() {
        return size;
    }

    public String getDesc() {
        return String.valueOf(desc);
    }

    /**
     * 添加国家
     */
    public void addCty() {

    }

}

