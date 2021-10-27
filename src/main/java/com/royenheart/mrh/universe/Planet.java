package com.royenheart.mrh.universe;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 行星对象
 * 包括行星数据：名字、背景故事、行星大小
 * 行星轨道数据：各个轨道距离、经济价值、占用情况
 * 卫星数据：对卫星对象的引用
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

    /** 行星轨道数据，行星生成后填入 */
    public ArrayList<Track> tracks = new ArrayList<>();

    /** 卫星数据，行星生成后填入 */
    public ArrayList<Satellite> sats = new ArrayList<>();

    /** 国家数据，行星生成后填入 */
    public ArrayList<Country> ctys = new ArrayList<>();

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
     * 根据传入的数组位置信息返回对应的轨道对象
     *
     * @param pos 需要的轨道对象在数组中的位置
     * @return 返回对应的轨道对象
     */
    public Track getTrackByPos(int pos) throws ArrayIndexOutOfBoundsException {
        return tracks.get(pos);
    }
}

