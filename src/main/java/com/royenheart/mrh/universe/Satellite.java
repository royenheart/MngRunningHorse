package com.royenheart.mrh.universe;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 卫星对象
 * <p>
 *     包括卫星数据：
 *     卫星名字、卫星cosparid、卫星轨道距离、卫星轨道价值、卫星状态、卫星所属国家
 *
 *     需实现方法：
 *     合法生成卫星、修改卫星状态并同步至行星轨道状态
 * </p>
 *
 * @author RoyenHeart
 */

public class Satellite {

    // 卫星轨道约束

    public static final double MIN_DIS = 1.2;
    public static final double MAX_DIS = Double.MAX_VALUE;
    public static final double MIN_VALUE = 0;

    // 卫星约束

    public static final int MAX_NAME_LENGTH = 10;
    public static final int MIN_NAME_LENGTH = 3;

    // 卫星数据

    private String name;
    private String cosparid;
    private BigDecimal distance;
    /**
     * 所处轨道价值
     */
    private double disValue;
    /** 是否正在使用 */
    private boolean used;
    private Country belongCty;

    public Satellite(String name, String cosparid, double distance, double disValue, boolean used) {
        this.name = String.valueOf(name);
        this.cosparid = String.valueOf(cosparid);
        this.distance = BigDecimal.valueOf(distance);
        this.disValue = disValue;
        this.used = used;
    }

    public void setCty(Country belongCty) {
        this.belongCty = belongCty;
    }

    /**
     * 返回卫星信息
     * <p>
     *     卫星名字、卫星轨道半径、卫星轨道价值、卫星cosparid、卫星所属国家、卫星是否仍在使用
     *     规范化字符串"%10s,%10f,%10f,%10s,%10s,%10s\n"
     * </p>
     *
     * @return 卫星信息
     */
    @Override
    public String toString() {
        return "" +
                String.format(
                        "%-16s%-16.2f%-16.2f%-16s%-16s%-16s\n",
                        name, distance, disValue, cosparid, belongCty.getName(), (used) ? "yes" : "no"
                );
    }

    /**
     * 比较两个卫星是否相同（cosparid编号不一样就行）
     *
     * @param o 比较的对象
     * @return 是否相同
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Satellite satellite = (Satellite) o;
        return cosparid.equals(satellite.getCosparid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cosparid);
    }

    public String getCosparid() {
        return String.valueOf(cosparid);
    }

    public Country getBelongCty() {
        return belongCty;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public double getDisValue() {
        return disValue;
    }

    public boolean getUsed() {
        return used;
    }

    public String getName() {
        return String.valueOf(name);
    }

    /**
     * 更改卫星使用状态，默认取反
     */
    public void setUsed() {
        this.used = !this.used;
    }

    /**
     * 更改卫星使用状态
     *
     * @param used 当前使用状态
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Set Satellite Name
     *
     * @param name the Satellite's name
     */
    public void setName(String name) {
        this.name = name;
    }

}
