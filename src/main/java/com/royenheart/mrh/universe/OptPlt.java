package com.royenheart.mrh.universe;

import java.security.Key;
import com.royenheart.mrh.opt.checkParam;
import java.util.*;

/**
 * 生成、载入、保存、操作行星及卫星操作
 *
 * @author RoyenHeart
 */
public class OptPlt {

    /**
     * 当前所选择的行星
     */
    private Planet plt = null;

    /**
     * 检测函数
     */
    private checkParam cp = null;

    /**
     * 添加卫星
     *
     * @return 是否添加成功
     */
    public boolean addSatellite() {

        Scanner text = new Scanner(System.in);

        String name;
        double dis;
        double value;
        String cosparid;
        Country belongCty;
        boolean used;

        Map<String, Object> params = new HashMap();
        params.put("name", name);
        params.put("track", dis);
        params.put("value", value);
        params.put("cosparid", cosparid);
        params.put("belongCty", belongCty);
        params.put("used", used);

        Set keySets = params.keySet();
        Iterator<String> keys = keySets.iterator();

        while (keys.hasNext()) {
            String err = "";
            String key = keys.next();
            do {
                System.out.print( err + "请填写" + key + ": ");
                String keyVal = text.nextLine();

            } while ()
        }

        return true;
    }

    /**
     * 打印出当前行星信息和卫星列表
     *
     * @return 行星信息和卫星列表
     */
    public String listInfo() {

        StringBuffer info = new StringBuffer("");

        info.append("Now shows the lists of planets and the satellites\n")
            .append("-----------------The Planet----------------------\n");

        info.append("The name: ").append(plt.getName())
            .append("The size: ").append(plt.getSize())
            .append("The background: \n").append(plt.getDesc());

        info.append("----------------The Satellites-------------------\n")
            .append(
                String.format(
                        "%10s,%10s,%10s,%10s,%10s\n",
                        "卫星名字", "卫星轨道半径", "卫星轨道价值", "卫星cosparid", "卫星所属城市", "是否可用"
                )
        );

        for (Satellite sat : plt.sats) {
            info.append(sat.toString());
        }

        info.append("---------------------End-------------------------\n");

        return info.toString();
    }

    /**
     * 编辑修改卫星信息
     *
     * @return 是否修改成功
     */
    public boolean editSat() {
        return true;
    }

    /**
     * 删除卫星
     *
     * @return 是否删除成功
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
    public String findSat(String name) {
        return null;
    }

    /**
     * 根据cosparid查找卫星并根据传入参数启用、封锁卫星
     *
     * @return 是否启用成功
     */
    public boolean activateSat(String cosparid, boolean isUse) {
        return false;
    }

    public boolean setPlt(Planet plt) {
        if (this.plt == null) {
            this.plt = plt;
            cp = new checkParam(plt);
            return true;
        } else {
            return false;
        }
    }

}

