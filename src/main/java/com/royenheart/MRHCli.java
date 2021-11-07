package com.royenheart;

import com.royenheart.mrh.opt.GamingOpt;
import com.royenheart.mrh.opt.LoadGame;

import java.io.IOException;

/**
 * 命令行方式运行飞马卫星管理程序
 *
 * @author RoyenHeart
 */
public class MRHCli {
    public static void main(String[] args) {

        // 初始化数据

        try {
            LoadGame.getLd().initial();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 操作提示
        String opInfo = "" +
                "=============================\n" +
                "   1---显示当前行星和活动卫星列表\n" +
                "   2---注册新卫星\n" +
                "   3---删除已有卫星\n" +
                "   4---激活卫星\n" +
                "   5---封锁卫星\n" +
                "   6---显示停运卫星列表\n" +
                "   7---查找卫星\n" +
                "   8---修改卫星信息\n" +
                "   9---添加国家\n" +
                "   10---退出!\n" +
                "=============================\n" +
                "选择: ";


        // 生成宇宙操作

        GamingOpt exec = new GamingOpt();

        String command;
        do {
            System.out.print(opInfo);
            command = SysIn.nextLine();

            // 判断指令是否处在0至10的范围内
            while (command.isEmpty() || command.matches(".*[^0-9].*") ||
                    Integer.parseInt(command) > 10 || Integer.parseInt(command) < 1) {
                System.out.println("Illegal command! Please insert a number range from 0-9!");
                command = SysIn.nextLine();
            }

            // 执行相应操作
            switch (Integer.parseInt(command)) {
                case 1:
                    exec.listInfo();
                    break;
                case 2:
                    exec.addSatellite();
                    break;
                case 3:
                    exec.delSat();
                    break;
                case 4:
                    exec.activateSat(true);
                    break;
                case 5:
                    exec.activateSat(false);
                    break;
                case 6:
                    exec.findUsedSat();
                    break;
                case 7:
                    exec.SatList();
                    break;
                case 8:
                    exec.editSat();
                    break;
                case 9:
                    exec.addCountry();
                    break;
                default: break;
            }

        } while (!command.matches("10"));

        System.exit(0);
    }
}
