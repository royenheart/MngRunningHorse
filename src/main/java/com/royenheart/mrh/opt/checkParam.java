package com.royenheart.mrh.opt;

import com.royenheart.mrh.universe.Planet;
import com.royenheart.mrh.universe.Track;

public class checkParam {

    private Planet plt;

    public checkParam(Planet plt) {
        this.plt = plt;
    }

    /**
     * 检测行星参数
     *
     * @param key 键
     * @param value 值
     * @return 返回错误信息err: message，检测通过返回ok
     */
    public String checkSat(String key, String value) {
        String status;

        switch (key) {
            case "name":
                status = checkSatName(value);
                break;
            case "dis":
                status = checkSatName(value);
                break;
            case "value":
                status = checkSatTVal(value);
                break;
            case "cosparid":
                break;
            case "belongCty":
                break;
            case "used":
                break;
            default:
                status = "err: 传入参数出错";
                break;
        }

        return status;
    }

    private String checkSatName(String value) {
        return value.isEmpty()?"err: 名字为空,":"ok";
    }

    private String checkSatDis(String value) {
        double dis;

        if (value.matches(".*[^0-9].*")) {
            return "err: 非法轨道编号,";
        } else {
            dis = Double.parseDouble(value);
            if (dis < Track.MIN_DIS || dis > Track.MAX_DIS) {
                return "err: 轨道不符合范围: [" + Track.MIN_DIS + "," + Track.MAX_DIS + "],";
            }
            for (Track t : plt.tracks) {
                if (Math.abs(dis - t.getDis()) < 0.2) {
                    return "err: 与" + t.getSat().getName() + "号" + "相距太近,";
                }
            }
            return "ok";
        }
    }

    private String checkSatTVal(String value) {
        double tVal;

        if (value.matches(".*[^0-9].*")) {
            return "err: 非法数值,";
        } else {
            tVal = Double.parseDouble(value);
            if (tVal < Track.MIN_VALUE) {
                return "err: 轨道过小,最小值" + tVal + ",";
            }
            return "ok";
        }
    }

    /**
     * 检测卫星cosparid是否合法
     *
     * @param value 传入的编号
     * @return 返回错误状态
     */
    private String checkSatCos(String value) {

    }

    private String checkSatCty() {

    }

    private String checkSatUsed() {

    }

}
