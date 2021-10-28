package com.royenheart.mrh.universe;

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

        String name = null;
        double dis = 0;
        double value = 0;
        String cosparid = null;
        Country belongCty = null;
        boolean used = false;

        ArrayList<String> params = new ArrayList<>();
        params.add("name");
        params.add("track");
        params.add("value");
        params.add("cosparid");
        params.add("belongCty");
        params.add("used");

        System.out.println("现在开始添加卫星，请添加对应的信息");
        System.out.println("卫星使用状态请填写true、false等真假字符");

        /* 遍历键值对 */
        for (String key : params ) {
            String err = "";
            String keyVal;

            /* 获取合法键值对 */
            do {
                System.out.print( err + "请填写" + key + ": ");
                keyVal = text.nextLine();
                err = cp.checkSat(key, keyVal);
            } while (err.contains("err:"));

            switch (key) {
                case "name":
                    name = keyVal;
                    break;
                case "dis":
                    dis = Double.parseDouble(keyVal);
                    break;
                case "value":
                    value = Double.parseDouble(keyVal);
                    break;
                case "cosparid":
                    cosparid = keyVal;
                    break;
                case "belongCty":
                    for (Country cty : plt.ctys) {
                        if (cty.getCode().equals(keyVal) || cty.getName().equals(keyVal)) {
                            belongCty = cty;
                            break;
                        }
                    }
                    break;
                case "used":
                    used = Boolean.parseBoolean(keyVal);
                    break;
                default:
                    break;
            }
        }

        // 添加卫星和对应轨道至行星

        Track newTrack = new Track(dis, value, used);
        Satellite newSat = new Satellite(name, newTrack, cosparid, belongCty, used, plt);

        plt.sats.add(newSat);
        plt.tracks.add(newTrack);

        return true;
    }

    /**
     * 打印出当前行星信息和卫星列表
     *
     * @return 行星信息和卫星列表
     */
    public String listInfo() {

        StringBuffer info = new StringBuffer();

        info.append("Now shows the lists of planets and the satellites\n")
            .append("-----------------The Planet----------------------\n");

        info.append("The name: ").append(plt.getName())
            .append("The size: ").append(plt.getSize())
            .append("The background: \n").append(plt.getDesc());

        info.append("----------------The Satellites-------------------\n")
            .append(
                String.format(
                        "%10s,%10s,%10s,%10s,%10s,%10s\n",
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
     * <p>
     *     1. 支持模糊查找
     *     2. 忽略大小写
     *     3. 根据名称查找卫星
     *     4. 根据cosparid查找卫星
     * </p>
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

    /**
     * 设置当前被操作的行星
     *
     * @param plt 行星对象
     * @return 是否成功
     */
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

