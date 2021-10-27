package com.royenheart.mrh.universe;

/**
 * 轨道对象
 */
public class Track {

    // 轨道约束

    public static final double MIN_DIS = 1.2;
    public static final double MAX_DIS = Double.MAX_VALUE;
    public static final double MIN_VALUE = 0;

    // 轨道数据

    private double dis;
    private double value;
    private boolean used;
    private Satellite sat;

    @Deprecated
    public Track() {
        this.dis = 1.2;
        this.value = 0;
        this.used = false;
    }

    public Track(double dis, double value, boolean used) {
        this.dis = dis;
        this.value = value;
        this.used = used;
    }

    public double getDis() {
        return dis;
    }

    public double getValue() {
        return value;
    }

    public boolean getUsed() {
        return used;
    }

    public Satellite getSat() {
        return sat;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
