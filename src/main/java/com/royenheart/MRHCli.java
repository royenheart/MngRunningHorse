package com.royenheart;

import com.royenheart.mrh.opt.LoadGame;
import com.royenheart.mrh.universe.universe;
import com.royenheart.mrh.universe.Planet;

import java.util.Scanner;

/**
 * 命令行方式运行飞马卫星管理程序
 *
 * @author RoyenHeart
 */
public class MRHCli {
    public static void main(String[] args) {

        // 载入界面
        LoadGame startMenu = new LoadGame();
        startMenu.initial();

        // 载入行星
        Planet plt = startMenu.getPlant();

        // 操作提示

        StringBuffer optInfoTmp = new StringBuffer("")
                .append("=============================\n")
                .append("   1---显示当前行星和活动卫星列表\n")
                .append("   2---注册新卫星\n")
                .append("   3---删除已有卫星\n")
                .append("   4---激活卫星\n")
                .append("   5---封锁卫星\n")
                .append("   6---显示停运卫星列表\n")
                .append("   7---按名称查找卫星\n")
                .append("   8---修改卫星信息\n")
                .append("   9---添加国家\n")
                .append("   10---退出!\n")
                .append("=============================\n")
                .append("选择: ");

        String opInfo = optInfoTmp.toString();

        /*
          操作具体实现
         */
        universe exec = new universe();
        exec.setPlt(plt);

        String command;
        do {
            System.out.print(opInfo);
            command = SysIn.nextLine();

            // 判断指令是否处在0至9的范围内
            while (command.isEmpty() || command.matches(".*[^0-9].*") || Integer.parseInt(command) > 9 || Integer.parseInt(command) < 1) {
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
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                default: break;
            }

        } while (!command.matches("9"));

        System.exit(0);
    }
}
