package com.royenheart.mrh.universe;

import java.util.Objects;

/**
 * 卫星对象
 * 包括卫星数据：
 * 卫星所处轨道、卫星cosparid、卫星所属国家、卫星状态、卫星名字、卫星所属行星
 *
 * 需实现方法：
 * 合法生成卫星、修改卫星状态并同步至行星轨道状态
 *
 * @author RoyenHeart
 */
public class Satellite {

    // 卫星数据

    private String name;
    private String cosparid;
    private Country belongCty;
    /** 是否正在使用 */
    private boolean used;
    /**
     * 所处轨道
     */
    private Track track;
    /** 所属行星 */
    private Planet belongPlt;

    public Satellite(String name, Track track, String cosparid, Country belongCty, boolean used, Planet belongPlt) {
        this.name = String.valueOf(name);
        this.track = track;
        this.cosparid = String.valueOf(cosparid);
        this.belongCty = belongCty;
        this.used = used;
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
        StringBuffer satInfo = new StringBuffer("");
        satInfo.append(
                String.format(
                        "%10s,%10f,%10f,%10s,%10s,%10s\n",
                        name, track.getDis(), track.getValue(), cosparid, belongCty, (used)?"是":"否"
                )
        );
        return satInfo.toString();
    }

    /**
     * 比较两个卫星是否相同（编号不一样就行）
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

    public Track getTrack() {
        return track;
    }

    public String getCosparid() {
        return String.valueOf(cosparid);
    }

    public String getBelongCty() {
        return belongCty.getName();
    }

    public boolean getUsed() {
        return used;
    }

    public String getName() {
        return String.valueOf(name);
    }

    /**
     * 卫星改名
     *
     * @param name 需要改成的名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 更改卫星使用状态
     *
     * @param used 当前使用状态
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

}
