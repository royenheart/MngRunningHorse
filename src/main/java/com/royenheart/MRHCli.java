package com.royenheart;

import com.royenheart.mrh.opt.LoadGame;
import com.royenheart.mrh.universe.OptPlt;

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

//        startMenu.initial();

        Scanner cliIn = new Scanner(System.in);

        // 操作提示

        StringBuffer optInfoTmp = new StringBuffer("")
                .append("=============================\n")
                .append("   1---显示当前活动卫星列表\n")
                .append("   2---注册新卫星\n")
                .append("   3---删除已有卫星\n")
                .append("   4---激活卫星\n")
                .append("   5---封锁卫星\n")
                .append("   6---显示停运卫星列表\n")
                .append("   7---按名称查找卫星\n")
                .append("   8---修改卫星信息\n")
                .append("   9---退出!\n")
                .append("=============================\n")
                .append("选择: ");

        String opInfo = optInfoTmp.toString();

        OptPlt exec = new OptPlt();

        String command;
        do {
            System.out.print(opInfo);
            command = cliIn.nextLine();

            // 判断指令是否处在0至9的范围内
            while (command.isEmpty() || command.matches(".*[^0-9].*") || Integer.valueOf(command) > 9 || Integer.valueOf(command) < 1) {
                System.out.println("Illegal command! Please insert a number range from 0-9!");
                command = cliIn.nextLine();
            }

            // 执行相应操作
            switch (Integer.valueOf(command)) {
                case 1:
                    break;
                case 2:
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
                default: break;
            }

        } while (!command.matches("9"));

        System.exit(0);
    }
}
