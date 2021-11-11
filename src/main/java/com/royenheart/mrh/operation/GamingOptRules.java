package com.royenheart.mrh.operation;

import com.royenheart.mrh.sysio.SysIn;
import com.royenheart.mrh.sysio.SysOutErr;
import com.royenheart.mrh.sysio.SysOutMain;
import com.royenheart.mrh.sysio.SysOutTip;

import java.lang.reflect.Method;

/**
 * 卫星查找规则集的操作
 * @author RoyenHeart
 */
public class GamingOptRules extends GamingOpt {

    private final SysOutMain out;
    private final SysOutTip tip;
    private final SysOutErr err;

    public GamingOptRules() {
        out = new SysOutMain();
        tip = new SysOutTip();
        err = new SysOutErr();
    }

    /**
     * 列出当前查找规则集
     * @param info 列表数据的存储
     * @return info
     */
    public String listRules(StringBuffer info) {
        info.append("\n当前查找规则\n");
        for (String key : RULES.keySet()) {
            info.append(key).append(":").append(getRulesOn(key) ? "是\n" : "否\n");
        }
        return info.toString();
    }

    /**
     * 配置查找规则集信息
     * @return 是否配置成功
     */
    public boolean setRules() {
        String command;

        out.print("\n现在开始配置卫星查找的规则集");
        out.print("请输入表示真假的单词设置规则开启状态");
        // 列出当前查找规则集
        out.print(listRules(new StringBuffer()));

        try {
            // 遍历键值对，获取规则和其对应的参数以及用户的输入
            for (String s : RULES.keySet()) {
                try {
                    Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
                    command = cp.checkParamMethod("请填写", s, useWhat, cp);
                } catch (NoSuchMethodException e) {
                    err.print("检测方法出错，请检查是否正确引用", e);
                    return false;
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
                    return false;
                }
                setRulesOn(s, cp.getTrueFalseFromIn(command));
            }

            // cosparid查找和name查找只留一个
            while (getRulesOn("name") == getRulesOn("cosparid")) {
                String open;
                if (getRulesOn("name") && getRulesOn("cosparid")) {
                    tip.print("名称查找和cosparid查找同时开启，请保留一个（name/cosparid）: ");
                } else {
                    tip.print("名称查找和cosparid查找全部关闭，请开启一个（name/cosparid）: ");
                }
                while (!(open = SysIn.nextLine()).matches("(^name$)|(^cosparid$)")) {
                    tip.print("参数错误！，请填写指定查找参数: ");
                }
                setRulesOn("name", false);
                setRulesOn("cosparid", false);
                setRulesOn(open, true);
            }
        } catch (ClassCastException e) {
            err.print("设置、获取规则开启情况key类型不匹配\n应为: String", e);
            return false;
        } catch (NullPointerException e) {
            err.print("key为空引用，请检查是否正确设置key的检测", e);
            return false;
        }

        return true;
    }

    /**
     * 卫星查找规则集指定规则开启状态根据传入的status设置
     * @param param 待查找的规则名
     * @param status 指定开启状态
     */
    public void setRulesOn(String param, boolean status) throws
            ClassCastException, NullPointerException {
        RULES_ON.set(RULES.get(param), status);
    }

    /**
     * 卫星查找规则集查询当前规则开启情况
     * @param param 待查找的规则名
     * @return 返回该规则开启情况
     */
    public boolean getRulesOn(String param) throws
            ClassCastException, NullPointerException {
        return RULES_ON.get(RULES.get(param));
    }

}
