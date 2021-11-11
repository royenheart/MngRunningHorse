package com.royenheart;

import com.royenheart.mrh.existence.Universe;
import com.royenheart.mrh.gamedata.DataRead;
import com.royenheart.mrh.gamedata.DataStore;
import com.royenheart.mrh.operation.*;
import com.royenheart.mrh.sysio.SysIn;
import com.royenheart.mrh.sysio.SysOutErr;
import com.royenheart.mrh.sysio.SysOutMain;
import com.royenheart.mrh.sysio.SysOutTip;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * 命令行方式运行飞马卫星管理程序
 * @author RoyenHeart
 */
public class MrhCli {
    public static void main(String[] args) {
        String command;
        Universe mng = Universe.getMng();
        CheckParam cp = new CheckParam();
        SysOutMain out = new SysOutMain();
        SysOutTip tip = new SysOutTip();
        SysOutErr err = new SysOutErr();

        try {
            // 初始行星数据
            DataRead.getLd().initial();
            // 初始化查找规则
            GamingOpt.initRules();
        } catch (IOException e) {
            err.print("读取数据出错，请检查行星数据存储位置是否正确", e);
            System.exit(-1);
        }

        out.print("欢迎来到" + Universe.getName() + "元宇宙!");
        if (mng.getAmountsPlts() <= 0) {
            tip.print("当前没有任何行星数据，请先创建一个新的行星");
            mng.initPlt();
        }
        while (true) {
            out.printOptPltChoose();
            command = cp.checkCommandInRange(SysIn.nextLine(), 1, 2);
            if (Integer.parseInt(command) == 1) {
                out.print(mng.listPlts());
                out.print("请选择需要游戏的行星前的编号");
                command = cp.checkCommandInRange(SysIn.nextLine(), 0, mng.getAmountsPlts()-1);
                mng.setPlt(Integer.parseInt(command));
                break;
            } else {
                out.print("当前已有行星\n"+mng.listPlts());
                mng.initPlt();
            }
        }

        // 操作键值对
        Hashtable<String, Method> optList = new Hashtable<>();
        Hashtable<Class, Object> optInstance = new Hashtable<>();
        try {
            optList.put("1", GamingOptListInfo.class.getMethod("listInfoAll"));
            optList.put("2", GamingOptSat.class.getMethod("addSat"));
            optList.put("3", GamingOptSat.class.getMethod("delSat"));
            optList.put("4", GamingOptSat.class.getMethod("disEnableSat"));
            optList.put("5", GamingOptSat.class.getMethod("findNoUseSat"));
            optList.put("6", GamingOptSat.class.getMethod("findSatListInfo"));
            optList.put("7", GamingOptSat.class.getMethod("editSat"));
            optList.put("8", GamingOptCty.class.getMethod("addCty"));
            optList.put("9", GamingOptCty.class.getMethod("listInfoCty"));
            optList.put("10", GamingOptRules.class.getMethod("setRules"));
            optInstance.put(GamingOptListInfo.class, new GamingOptListInfo());
            optInstance.put(GamingOptRules.class, new GamingOptRules());
            optInstance.put(GamingOptCty.class, new GamingOptCty());
            optInstance.put(GamingOptSat.class, new GamingOptSat());
        } catch (NoSuchMethodException e) {
            err.print("未找到相关方法，请检查操作列表是否添加正确?", e);
            System.exit(-1);
        }

        do {
            boolean isOptSuccess;
            out.printOptCommand();
            // 判断是否为正整数且处于1-11的范围内
            command = cp.checkCommandInRange(SysIn.nextLine(), 1, 11);

            // 执行相应操作
            if (!"11".equals(command)) {
                try {
                    Method judge = optList.get(command);
                    Class who = judge.getDeclaringClass();
                    isOptSuccess = (boolean) judge.invoke(optInstance.get(who));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    err.print("操作拒绝访问或掷出错误未被处理，终止进一步操作，请检查错误", e);
                    break;
                } catch (IllegalArgumentException e) {
                    err.print("传入错误/多余参数至方法，请检查参数");
                    break;
                }
                tip.print((isOptSuccess?"操作成功":"操作失败"));
            }
        } while (!command.matches("11"));

        tip.print("程序正在退出中，请勿关闭程序");
        // 保存修改的行星数据
        DataStore.getQg().store();
        System.exit(0);
    }
}