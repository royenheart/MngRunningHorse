package com.royenheart.mrh.operation;

import com.royenheart.SysIn;
import com.royenheart.mrh.existence.Country;
import com.royenheart.mrh.existence.Planet;
import com.royenheart.mrh.existence.Satellite;
import com.royenheart.mrh.existence.Universe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 检测错误参数
 * <p>
 *     使用相关函数，填入对应待检测的参数
 *     统一返回"err: "错误提醒
 *     用于GamingOpt类的操作错误提醒
 * </p>
 * @author RoyenHeart
 */
public class CheckParam {

    // Universe实例（方便调用）

    private final Universe mng;

    // 真假判断单词集

    private static final String[] TRUTHS = {"true","yes","真","是","y","ok"};
    private static final String[] FALSES = {"false","no","假","否","n"};

    // 单例设计模式，只允许生成一个CheckParam类，并托付给LoadGame

    private static final CheckParam CP = new CheckParam();
    private CheckParam() {
        mng = Universe.getMng();
    }
    public static CheckParam getCp() {
        return CP;
    }

    /**
     * 检查卫星名字
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
     * @param value 用户填入指令
     * @return 错误状态
     */
    public String checkSatDis(String value) {
        double dis;

        if (value.isEmpty()) {
            return "err: 输入为空,请填入非空合法半径,";
        }else if (!checkNumDecimal(value)) {
            return "err: 非法轨道半径,";
        } else {
            dis = Double.parseDouble(value);
            if (!checkNumDecRange(dis, Satellite.MIN_DIS, Satellite.MAX_DIS)) {
                return "err: 轨道不符合范围: [" + Satellite.MIN_DIS + "," + Satellite.MAX_DIS + "]，";
            }
            for (Country cty : mng.getPlt().getCtys()) {
                for (Satellite e : cty.getSats()) {
                    if (e.getDistance().subtract(BigDecimal.valueOf(dis)).abs().compareTo(BigDecimal.valueOf(0.2)) < 0
                    ) {
                        return "err: 与" + e.getName() + "号" + "[轨道距离：" + e.getDistance() + "]" + "相距太近，";
                    }
                }
            }
            return "ok!";
        }
    }

    /**
     * 检测轨道价值
     * @param param 用户填入参数
     * @return 错误状态
     */
    public String checkSatTruckValue(String param) {
        double value;

        if (param.isEmpty()) {
            return "err: 输入为空,请填入非空合法轨道价值,";
        } else if (!checkNumDecimal(param)) {
            return "err: 非法轨道价值，";
        } else {
            value = Double.parseDouble(param);
            if (!checkNumDecRange(value, Satellite.MIN_VALUE, Satellite.MAX_VALUE)) {
                return "err: 轨道价值不符合范围: [" + Satellite.MIN_VALUE + "," + Satellite.MAX_VALUE + "]，";
            }
            return "ok!";
        }
    }

    /**
     * 检测卫星cosparid是否合法
     * @param value 传入的编号
     * @return 返回错误状态
     */
    public String checkSatCos(String value) {
        if (value.isEmpty()) {
            return "err: 输入为空，请输入非空合法cosparid";
        } else if (value.length() != 6) {
            return (value.length() < 6)?"err: cosparid过短，":"err: cosparid过长，";
        } else {
            if (value.matches("^[A-Za-z]{2}[0-9]{4}$")) {
                // 检测是否有cosparid冲突并判断是否属于现存的某个国家
                boolean isBelong = false;
                for (Country cty : Universe.getMng().getPlt().getCtys()) {
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
     * @param value 用户输入
     * @return 错误状态
     */
    public String checkSatUsed(String value) {
        return checkTrueFalse(value);
    }

    /**
     * 检查国家名字是否合法
     * @param param 国家名字
     * @return 错误状态
     */
    public String checkCtyName(String param) {
        if (param.isEmpty()) {
            return "err: 国家名字为空，";
        } else if (param.length() > Country.MAX_NAME_LENGTH) {
            return "err: 国家名字过长，请限制于" + Country.MAX_NAME_LENGTH + "个字符以内，";
        } else if (param.length() < Country.MIN_NAME_LENGTH) {
            return "err: 国家名字过短，请大于等于" + Country.MIN_NAME_LENGTH + "个字符，";
        } else {
            return "ok!";
        }
    }

    /**
     * 检查国家编号是否合法
     * @param param 国家编号
     * @return 错误状态
     */
    public String checkCtyCode(String param) {
        if (param.isEmpty()) {
            return "err: 编号为空，";
        } else if (!param.matches("[A-Za-z]{2}")) {
            return "err: 编号格式错误，应为两位英文字母组成的编号，";
        } else {
            for (Country cty : mng.getPlt().getCtys()) {
                if (cty.getCode().equals(param.toUpperCase())) {
                    return "err: 编号冲突，与" + cty.getCode() + "发生碰撞，";
                }
            }
            return "ok!";
        }
    }

    /**
     * 判断输入是否为表示真假
     * @param param 输入值
     * @return 错误状态
     */
    public String checkTrueFalse(String param) {
        int i;
        if (param.isEmpty()) {
            return "err: 输入空字串，请输入合法单词,";
        }
        for (i = 0; i < TRUTHS.length; i++) {
            if (TRUTHS[i].equals(param.toLowerCase())) {
                return "ok!";
            }
        }
        for (i = 0; i < FALSES.length; i++) {
            if (FALSES[i].equals(param.toLowerCase())) {
                return "ok!";
            }
        }
        return "err: 输入字符不表示真假,";
    }

    /**
     * 从输入获取真假
     * @param param 用户输入
     * @return 用户输入等价的真假
     */
    public boolean getTrueFalseFromIn(String param) {
        int i;
        for (i = 0; i < TRUTHS.length; i++) {
            if (TRUTHS[i].equals(param.toLowerCase())) {
                return true;
            }
        }
        for (i = 0; i < FALSES.length; i++) {
            if (FALSES[i].equals(param.toLowerCase())) {
                return false;
            }
        }
        return false;
    }

    /**
     * 检查行星名称
     * @param param 用户输入
     * @return 错误状态
     */
    public String checkPltName(String param) {
        if (param.isEmpty()) {
            return "err: 行星名称为空,";
        } else if (param.length() > Planet.MAX_NAME_SIZE) {
            return "err: 行星名字过长，请限制于" + Planet.MAX_NAME_SIZE + "个字符以内，";
        } else if (param.length() < Planet.MIN_NAME_SIZE) {
            return "err: 行星名字过短，请大于等于" + Planet.MIN_NAME_SIZE + "个字符，";
        } else {
            for (Planet plt : mng.getPlts()) {
                if (plt.getName().equals(param)) {
                    return "err: 行星名称冲突，请换一个,";
                }
            }
        }
        return "ok!";
    }

    /**
     * 检查行星尺寸
     * @param param 用户输入
     * @return 错误状态
     */
    public String checkPltSize(String param) {
        try {
            if (param.isEmpty()) {
                return "err: 行星尺寸不得为空,";
            } else if (!checkNumPositive(param)) {
                return "err: 异常字符,";
            } else if (Integer.parseInt(param) < Planet.MIN_SIZE) {
                return "err: 行星尺寸过小[<" + Planet.MIN_SIZE + "],";
            } else if (Integer.parseInt(param) > Planet.MAX_SIZE) {
                return "err: 行星尺寸过大[>" + Planet.MAX_SIZE + "],";
            }
        } catch (NumberFormatException e) {
            return "err: String转int类型出错，超出int类型最大范围或不符合int格式,";
        }
        return "ok!";
    }

    /**
     * 判断字符输入是否为正实数（包括小数）
     * @param value 用户输入
     * @return 判断结果
     */
    public boolean checkNumDecimal(String value) {
        return value.matches("(^[0-9]*$)|(^[0-9]+\\.[0-9]+$)");
    }

    /**
     * 判断数字是否为正整数
     * @param value 用户输入
     * @return 判断结果
     */
    public boolean checkNumPositive(String value) {
        return value.matches("^[0-9]+$");
    }

    /**
     * 判断一定范围内的正整数(闭区间)
     * @param value 用户输入
     * @param low 最小值
     * @param high 最大值
     * @return 判断结果
     */
    public boolean checkNumPosRange(int value, int low, int high) {
        return value >= low && value <= high;
    }

    /**
     * 判断一定范围内的正实数(闭区间)
     * @param value 用户输入正实数
     * @param low 最小值
     * @param high 最大值
     * @return 判断结果
     */
    public boolean checkNumDecRange(double value, double low, double high) {
        return value <= high && value >= low;
    }

    /**
     * 判断输入是否为处于某个范围内的正整数（用于指令等的选择)
     * <p>
     *     >low && <high
     * </p>
     * @param low 最小值
     * @param high 最大值
     */
    public String checkCommandInRange(String command ,int low, int high) {
        while (command.isEmpty() || !checkNumPositive(command) ||
                !checkNumPosRange(Integer.parseInt(command), low, high)) {
            System.out.println("非法操作! 请键入数字，范围为 " + (low+1) + "-" + (high-1) + "!");
            command = SysIn.nextLine();
        }
        return command;
    }

    /**
     * 检查各种错误，以相同模板设置提示
     * @param message 输入提示信息
     * @param param 需要被输入的参数或者额外说明/限制
     * @param checkMethod 规范输入的检测方法
     * @return 最终经规范后的用户输入
     */
    public String checkParamMethod(String message, String param, Method checkMethod)
            throws IllegalAccessException, IllegalArgumentException {
        String err = "";
        String command;
        do {
            System.out.print(err + message + param + ": ");
            command = SysIn.nextLine();
            try {
                err = (String) checkMethod.invoke(CheckParam.getCp(), command);
            } catch (InvocationTargetException e) {
                System.out.println("检测方法出现错误");
                e.printStackTrace();
            }
        } while (err.contains("err:"));
        return command;
    }

}