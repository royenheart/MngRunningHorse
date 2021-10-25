package mrh.universe;

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

    // 行星轨道数据，行星生成后填入

    // 卫星数据，行星生成后填入

    // 国家数据，行星生成后填入

    // 覆写equals方法


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

}

/**
 * 轨道对象
 */
class Track {

    // 轨道约束

    public static final double MIN_DIS = 1.2;
    public static final double MAX_DIS = Double.MAX_VALUE;
    public static final double MIN_VALUE = 0;

    // 轨道数据

    private double dis;
    private double value;
    private boolean has;
    private boolean used;

    @Deprecated
    public Track() {

        this.dis = 1.2;
        this.value = 0;
        this.has = false;
        this.used = false;
    }

    public Track(double dis, double value, boolean has, boolean used) {

        this.dis = dis;
        this.value = value;
        this.has = has;
        this.used = used;
    }

    public double getDis() {
        return dis;
    }

    public double getValue() {
        return value;
    }

    public boolean getHas() {
        return has;
    }

    public boolean getUsed() {
        return used;
    }
}

/**
 * 国家对象
 */
class Country {

    // 国家数据

    private String name;
    private String code;

    @Deprecated
    public Country() {

    }

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

}
