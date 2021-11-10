package com.royenheart;

import com.royenheart.mrh.operation.CheckParam;
import com.royenheart.mrh.operation.GamingOpt;
import com.royenheart.mrh.gamedata.DataRead;
import com.royenheart.mrh.gamedata.DataStore;
import com.royenheart.mrh.existence.Universe;

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
        CheckParam cp = CheckParam.getCp();
        GamingOpt exec = GamingOpt.getExec();

        // 初始化数据
        try {
            DataRead.getLd().initial();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 选择、生成行星
        String uniInfo = "" +
                "=============================\n" +
                "   1---选择行星\n" +
                "   2---生成新行星\n" +
                "=============================\n" +
                "选择: ";

        System.out.println("欢迎来到" + Universe.getName() + "元宇宙!");
        int com;
        do {
            if (mng.getAmountsPlts() <= 0) {
                System.out.println("未检测到任何已有行星数据，请先创建一个新的行星");
                com = 2;
                mng.initPlt();
            } else {
                System.out.println(uniInfo);
                command = SysIn.nextLine();
                // 判断指令是否为数字的范围内
                command = cp.checkCommandInRange(command, 1, 2);
                com = Integer.parseInt(command);
                if (com == 1) {
                    System.out.println(mng.listPlts());
                    System.out.println("选择行星，请选择行星前的编号");
                    command = SysIn.nextLine();
                    command = cp.checkCommandInRange(command, 0, mng.getAmountsPlts()-1);
                    mng.setPlt(Integer.parseInt(command));
                } else {
                    mng.initPlt();
                }

                mng.setPlt(Integer.parseInt(command));
            }
        } while (com != 1);

        // 操作提示
        String opInfo = "" +
                "=============================\n" +
                "   1---显示当前行星和活动卫星列表\n" +
                "   2---注册新卫星\n" +
                "   3---删除已有卫星\n" +
                "   4---激活、封锁卫星\n" +
                "   5---显示停运卫星列表\n" +
                "   6---查找卫星\n" +
                "   7---修改卫星信息\n" +
                "   8---添加国家\n" +
                "   9---显示当前国家列表\n" +
                "   10---卫星搜索配置\n" +
                "   11---退出!\n" +
                "=============================\n" +
                "选择: ";

        // 操作键值对
        Hashtable<String, Method> optList = new Hashtable<>();
        try {
            optList.put("1", GamingOpt.class.getMethod("listInfoAll"));
            optList.put("2", GamingOpt.class.getMethod("addSat"));
            optList.put("3", GamingOpt.class.getMethod("delSat"));
            optList.put("4", GamingOpt.class.getMethod("disEnableSat"));
            optList.put("5", GamingOpt.class.getMethod("findNoUseSat"));
            optList.put("6", GamingOpt.class.getMethod("findSatListInfo"));
            optList.put("7", GamingOpt.class.getMethod("editSat"));
            optList.put("8", GamingOpt.class.getMethod("addCty"));
            optList.put("9", GamingOpt.class.getMethod("listInfoCty"));
            optList.put("10", GamingOpt.class.getMethod("setRules"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        do {
            boolean isOptSuccess = false;
            System.out.print(opInfo);
            command = SysIn.nextLine();

            // 判断是否为正整数且处于1-11的范围内
            command = cp.checkCommandInRange(command, 1, 11);

            // 执行相应操作
            if ("11".equals(command)) {
                isOptSuccess = true;
            } else {
                try {
                    isOptSuccess = (boolean) optList.get(command).invoke(exec);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            if (isOptSuccess) {
                System.out.println("操作成功");
            } else {
                System.out.println("操作失败");
            }
        } while (!command.matches("11"));

        System.out.println("程序正在退出中，请勿关闭程序");
        if (DataStore.getQg().store()) {
            System.out.println("已保存文件!");
        } else {
            System.out.println("保存文件出错!");
        }

        System.exit(0);
    }
}
