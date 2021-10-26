package com.royenheart.mrh.universe;

/**
 * 生成、载入、保存、操作行星及卫星
 * 行星卫星数据保存至resources/data的json文件内
 * 以行星名字来命名
 *
 * @author RoyenHeart
 */
public class OptPlt {

    public Planet addPlanet() {
        return null;
    }

    public Satellite addSatellite() {
        return null;
    }

    /**
     * 打印出当前行星信息和卫星列表
     *
     * @return 行星信息和卫星列表
     */
    public String listInfo() {

        StringBuffer info = new StringBuffer("");

        info.append("Now shows the lists of planets and the satellites\n"+
                    "-----------------The Planet----------------------\n");



        info.append("----------------The Satellites-------------------\n");



        info.append("---------------------End-------------------------\n");

        return info.toString();
    }

    /**
     * 编辑修改卫星信息
     *
     * @return
     */
    public boolean editSat() {
        return true;
    }

    /**
     * 删除卫星
     *
     * @return
     */
    public boolean delSat() {
        return true;
    }

    /**
     * 查找卫星：
     * 1. 支持模糊查找
     * 2. 忽略大小写
     * 3. 根据名称查找卫星
     * 4. 根据cosparid查找卫星
     *
     * @return 查找到的卫星信息(卫星信息覆写toString方法)
     */
    public Satellite findSat(String name) {
        return null;
    }

    /**
     * 根据cosparid查找卫星并根据传入参数启用、封锁卫星
     *
     * @return
     */
    public boolean activateSat(String cosparid, boolean isUse) {
        return false;
    }


}

/**
 * 行星、卫星、轨道、国家等参数检验，检验不通过不能执行
 */
class CheckParams {

    @Deprecated
    public CheckParams() {

    }

    /**
     * 判断行星参数是否正确
     * 判断条件：
     * 1. 行星名字、介绍具备 √
     * 2. 行星大小合适 √
     * 3. 不能有多个相同行星（覆写equals方法）
     *
     * @return 是否正常生成
     */
    public static boolean checkPlanet(String name, String desc, int size) {
        if (name != null && desc != null && size > Planet.MIN_SIZE && size < Planet.MAX_SIZE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断卫星参数是否正确
     * 判断条件：
     * 1. 卫星名字、cosparid具备
     * 2. 卫星具备唯一的cosparid（覆写equals方法）
     *
     * @return 是否正常生成
     */
    public static boolean checkSat() {
        return true;
    }

    /**
     * 判断轨道参数是否正确
     * 判断条件：
     * 1. 轨道距离大于等于1.2
     * 2. 相邻轨道间距不小于0.2（distance方法）
     * 3. 轨道价值合适
     * 4. 轨道卫星在轨和在使用不冲突（只允许在轨使用和在轨不使用）
     *
     * @return 是否正常生成
     */
    public static boolean checkTra(double dis, double value, boolean has, boolean used) {
        return true;
    }

    /**
     * 判断国家参数是否正确
     * 判断条件：
     * 1. 国家名字、编码具备
     * 2. 国家编码唯一（覆写equals方法）
     *
     * @return 是否正常生成
     */
    public static boolean checkCty() {
        return true;
    }

}