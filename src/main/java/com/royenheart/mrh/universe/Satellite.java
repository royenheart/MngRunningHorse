package com.royenheart.mrh.universe;

import com.google.gson.annotations.Expose;
import com.royenheart.mrh.opt.LoadGame;

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
    public static final double MAX_DIS = 999;
    public static final double MIN_VALUE = 0.1;
    public static final double MAX_VALUE = 999;

    // 卫星约束

    public static final int MAX_NAME_LENGTH = 10;
    public static final int MIN_NAME_LENGTH = 3;

    // 卫星数据

    @Expose
    private String name;
    @Expose
    private String cosparid;
    @Expose
    private BigDecimal distance;
    /**
     * 所处轨道价值
     */
    @Expose
    private double disValue;
    /** 是否正在使用 */
    @Expose
    private boolean used;

    public Satellite(String name, String cosparid, double distance, double disValue, boolean used) {
        this.name = name;
        this.cosparid = cosparid.toUpperCase();
        this.distance = BigDecimal.valueOf(distance);
        this.disValue = disValue;
        this.used = used;
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
                        "%-13s%-12.2f%-12.2f%-11s%-12s%-7s\n",
                        name, distance, disValue, cosparid, inWhichCountry().getName(), (used) ? "yes" : "no"
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

    public BigDecimal getDistance() {
        return distance;
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
     * 设置卫星名字
     *
     * @param name the Satellite's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置轨道价值
     *
     * @param disVal 设置的轨道价值
     */
    public void setDisValue(double disVal) {
        this.disValue = disVal;
    }

    public void setDisValue(String disVal) {
        this.disValue = Double.parseDouble(disVal);
    }

    public Country inWhichCountry() {
        for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
            if (cty.getSats().contains(this)) {
                return cty;
            }
        }
        return null;
    }

}
