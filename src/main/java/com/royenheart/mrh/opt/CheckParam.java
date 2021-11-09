package com.royenheart.mrh.opt;

import com.royenheart.mrh.universe.Country;
import com.royenheart.mrh.universe.Satellite;

import java.math.BigDecimal;

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

    private static final String[] trues = {"true","yes","真","是","y","ok"};
    private static final String[] falses = {"false","no","假","否","n"};

    // 单例设计模式，只允许生成一个CheckParam类，并托付给LoadGame
    private static final CheckParam CP = new CheckParam();
    private CheckParam() {}
    public static CheckParam getCp() {
        return CP;
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
            case "track_value":
                status = checkSatTruckValue(value);
                break;
            case "cosparid":
                status = checkSatCos(value);
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
            return "err: 名字过长，请限制于" + Satellite.MAX_NAME_LENGTH + "个字符以内，";
        } else if (value.length() < Satellite.MIN_NAME_LENGTH) {
            return "err: 名字过短，请大于等于" + Satellite.MIN_NAME_LENGTH + "个字符，";
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
            for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
                for (Satellite e : cty.getSats()) {
                    if (e.getDistance().subtract(BigDecimal.valueOf(dis)).abs().compareTo(BigDecimal.valueOf(0.2)) < 0) {
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
    public String checkSatTruckValue(String value) {
        double tVal;

        if (!value.matches("([1-9][0-9]+)|([0-9])|([0-9].[0-9]+)|([1-9][0-9]+.[0-9]+)")) {
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
            if (value.matches("^[A-Za-z]{2}[0-9]{4}$")) {
                // 检测是否有cosparid冲突并判断是否属于现存的某个国家
                boolean isBelong = false;
                for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
                    if (cty.getCode().equals(value.toUpperCase().substring(0,2))) {
                        isBelong = true;
                    }
                    for (Satellite sat : cty.getSats()) {
                        if (sat.getCosparid().equals(value.toUpperCase())) {
                            return "err: cosparid已存在\n" + "冲突卫星: \n" + sat.getName() + ":" + sat.getCosparid() + "\n";
                        }
                    }
                }
                if (!isBelong) {
                    return "err: cosparid前两位国家编号未找到对应国家，请退出当前操作建立新国家后再来，";
                }
                return "ok!";
            } else {
                return "err: cosparid格式错误，正确格式为两位国家编号(英文字母)+4位数字，";
            }
        }
    }

    /**
     * 检查卫星使用状态是否合法
     *
     * @return 错误状态
     */
    public String checkSatUsed(String value) {
        return checkTrueFalse(value);
    }

    /**
     * 检查添加国家操作是否合法
     *
     * @param name 国家名字
     * @param code 国家编号
     * @return 错误状态
     */
    public String checkCtyAdd(String name, String code) {
        if (name.isEmpty() | code.isEmpty()) {
            return "err: 国家名字或编号为空，";
        } else if (name.length() > Country.MAX_NAME_LENGTH) {
            return "err: 国家名字过长，请限制于" + Country.MAX_NAME_LENGTH + "个字符以内，";
        } else if (name.length() < Country.MIN_NAME_LENGTH) {
            return "err: 国家名字过短，请大于等于" + Country.MIN_NAME_LENGTH + "个字符，";
        } else if (!code.matches("[A-Za-z]{2}")) {
            return "err: 国家编号格式错误，应为两位英文字母组成的编号，";
        } else {
            for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
                if (cty.getCode().equals(code.toUpperCase())) {
                    return "err: 编号冲突，与" + cty.getCode() + "发生碰撞，";
                }
            }
            return "ok!";
        }
    }

    /**
     * 判断输入是否为表示真假
     *
     * @param param 输入值
     * @return 错误状态
     */
    public String checkTrueFalse(String param) {
        int i;
        for (i = 0;i < trues.length;i++) {
            if (trues[i].equals(param.toLowerCase())) {
                return "ok!";
            }
        }
        for (i = 0;i < falses.length;i++) {
            if (falses[i].equals(param.toLowerCase())) {
                return "ok!";
            }
        }
        return "err: 输入字符不表示真假,";
    }

    public boolean getTrueFalseFromIn(String param) {
        int i;
        for (i = 0;i < trues.length;i++) {
            if (trues[i].equals(param.toLowerCase())) {
                return true;
            }
        }
        for (i = 0;i < falses.length;i++) {
            if (falses[i].equals(param.toLowerCase())) {
                return false;
            }
        }
        return false;
    }


}