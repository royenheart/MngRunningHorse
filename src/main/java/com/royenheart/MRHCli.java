package com.royenheart;

import com.royenheart.mrh.opt.GamingOpt;
import com.royenheart.mrh.opt.LoadGame;
import com.royenheart.mrh.opt.QuitGame;

import java.io.IOException;

/**
 * 命令行方式运行飞马卫星管理程序
 *
 * @author RoyenHeart
 */
public class MRHCli {
    public static void main(String[] args) {

        String command;

        // 初始化数据
        try {
            LoadGame.getLd().initial();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 选择、删除、生成行星
        System.out.println(LoadGame.MNG.listPlts());
        System.out.println("请键入序号选择此次游戏的行星");
        command = SysIn.nextLine();
        // 判断指令是否为数字的范围内
        while (command.isEmpty() || command.matches(".*[^0-9].*") || !LoadGame.MNG.setPlt(Integer.parseInt(command))) {
            System.out.println("非法操作! 请键入数字编号或者键入正确的范围!");
            command = SysIn.nextLine();
        }

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
                "   10---退出!\n" +
                "=============================\n" +
                "选择: ";


        // 生成宇宙操作

        GamingOpt exec = new GamingOpt();

        do {
            System.out.print(opInfo);
            command = SysIn.nextLine();
            boolean isOptSuccess = false;

            // 判断指令是否处在1至10的范围内
            while (command.isEmpty() || command.matches(".*[^0-9].*") ||
                    Integer.parseInt(command) > 10 || Integer.parseInt(command) < 1) {
                System.out.println("非法操作! 请键入数字，范围为 1-10!");
                command = SysIn.nextLine();
            }

            // 执行相应操作
            switch (Integer.parseInt(command)) {
                case 1:
                    isOptSuccess = exec.listInfo();
                    break;
                case 2:
                    isOptSuccess = exec.addSatellite();
                    break;
                case 3:
                    isOptSuccess = exec.delSat();
                    break;
                case 4:
                    isOptSuccess = exec.activateSat();
                    break;
                case 5:
                    isOptSuccess = exec.findUsedSat();
                    break;
                case 6:
                    isOptSuccess = exec.SatList();
                    break;
                case 7:
                    isOptSuccess = exec.editSat();
                    break;
                case 8:
                    isOptSuccess = exec.addCountry();
                    break;
                case 9:
                    isOptSuccess = exec.listInfoCty();
                    break;
                case 10:
                    isOptSuccess = true;
                    break;
                default: break;
            }

            if (isOptSuccess) {
                System.out.println("操作成功");
            } else {
                System.out.println("操作失败");
            }

        } while (!command.matches("10"));

        System.out.println("程序正在退出中，请勿关闭程序");
        QuitGame.getQG().store();

        System.exit(0);
    }
}
