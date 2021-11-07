package com.royenheart.mrh.opt;

import com.royenheart.mrh.universe.Country;
import com.royenheart.mrh.universe.Satellite;

/**
 * 检测错误参数
 * <p>
 *     使用相关函数，填入对应待检测的参数
 *     统一返回"err: "错误提醒
 *     用于GamingOpt类的操作错误提醒
 * </p>
 *
 * @author RoyenHeart
 */
public class CheckParam {

    // 单例设计模式，只允许生成一个CheckParam类，并托付给LoadGame

    private static CheckParam cp = new CheckParam();
    private CheckParam() {}
    public static CheckParam getCp() {
        return cp;
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
            case "distance":
                status = checkSatDis(value);
                break;
            case "value":
                status = checkSatTVal(value);
                break;
            case "cosparid":
                status = checkSatCos(value);
                break;
            case "belongCty":
                status = checkSatCty(value);
                break;
            case "used":
                status = checkSatUsed(value);
                break;
            default:
                status = "err: 传入参数出错，";
                break;
        }

        return status;
    }

    /**
     * 检查卫星名字
     *
     * @param value 待检查的卫星名字
     * @return 返回错误信息
     */
    public String checkSatName(String value) {
        if (value.isEmpty()) {
            return "err: 名字为空，";
        } else if (value.length() > Satellite.MAX_NAME_LENGTH) {
            return "err: 名字过长，请限制于10个字符以内，";
        } else if (value.length() < Satellite.MIN_NAME_LENGTH) {
            return "err: 名字过短，请大于等于3个字符，";
        } else {
            return "ok!";
        }
    }

    /**
     * 检测轨道半径
     *
     * @param value 用户填入指令
     * @return 错误状态
     */
    public String checkSatDis(String value) {
        double dis;

        if (!value.matches("([1-9][0-9]+)|([0-9])|([0-9].[0-9]+)|([1-9][0-9]+.[0-9]+)")) {
            return "err: 非法轨道半径，";
        } else {
            dis = Double.parseDouble(value);
            if (dis < Satellite.MIN_DIS || dis > Satellite.MAX_DIS) {
                return "err: 轨道不符合范围: [" + Satellite.MIN_DIS + "," + Satellite.MAX_DIS + "]，";
            }
            for (Country cty : LoadGame.mng.getPlt().ctys) {
                for (Satellite e : cty.sats) {
                    if (Math.abs(dis - e.getDistance()) < 0.2) {
                        return "err: 与" + e.getName() + "号" + "[轨道距离：" + e.getDistance() + "]" + "相距太近，";
                    }
                }
            }
            return "ok!";
        }
    }

    /**
     * 检测轨道价值
     *
     * @param value 用户填入指令
     * @return 错误状态
     */
    public String checkSatTVal(String value) {
        double tVal;

        if (value.matches(".*[^0-9].*")) {
            return "err: 非法数值，";
        } else {
            tVal = Double.parseDouble(value);
            if (tVal < Satellite.MIN_VALUE) {
                return "err: 轨道半径过小,最小值" + tVal + "，";
            }
            return "ok!";
        }
    }

    /**
     * 检测卫星cosparid是否合法
     *
     * @param value 传入的编号
     * @return 返回错误状态
     */
    public String checkSatCos(String value) {
        if (value.length() != 6) {
            return (value.length() < 6)?"err: cosparid过短，":"err: cosparid过长，";
        } else {
            if (value.matches("^[A-Z]{2}[0-9]{4}$")) {
                // 检测是否有cosparid冲突
                for (Country cty : LoadGame.mng.getPlt().ctys) {
                    for (Satellite sat : cty.sats) {
                        if (sat.getCosparid().equals(value)) {
                            return "err: cosparid已存在\n" + "冲突卫星: \n" + sat.getName() + sat.getCosparid() + "\n";
                        }
                    }
                }
                return "ok!";
            } else {
                return "err: cosparid格式错误，正确格式为两位国家大写编号+4位数字，";
            }
        }
    }

    /**
     * 检查所属国家是否合法
     *
     * @return 错误状态
     */
    public String checkSatCty(String value) {
        for (Country cty : LoadGame.mng.getPlt().ctys) {
            if (cty.getName().equals(value) || cty.getCode().equals(value)) {
                return "ok!";
            }
        }
        return "err: 没有指定的国家，需自行创建，";
    }

    /**
     * 检查卫星使用状态是否合法
     *
     * @return 错误状态
     */
    public String checkSatUsed(String value) {
        if ("true".equals(value) || "false".equals(value)) {
            return "ok!";
        }
        return "err: 使用状态参数错误，";
    }

}
