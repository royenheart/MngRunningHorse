package com.royenheart;

import com.royenheart.mrh.opt.GamingOpt;
import com.royenheart.mrh.opt.LoadGame;
import com.royenheart.mrh.opt.QuitGame;
import com.royenheart.mrh.universe.Universe;

import java.io.IOException;

/**
 * 命令行方式运行飞马卫星管理程序
 *
 * @author RoyenHeart
 */
public class MrhCli {
    public static void main(String[] args) {
        String command;
        GamingOpt exec = GamingOpt.getExec();

        // 初始化数据
        try {
            LoadGame.getLd().initial();
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
            if (LoadGame.MNG.getAmountsPlts() <= 0) {
                System.out.println("未检测到任何已有行星数据，请先创建一个新的行星");
                com = 2;
                LoadGame.MNG.initPlt();
            } else {
                System.out.println(uniInfo);
                command = SysIn.nextLine();
                // 判断指令是否为数字的范围内
                while (command.isEmpty() || command.matches(".*[^0-9].*") ||
                        Integer.parseInt(command) < 1 || Integer.parseInt(command) > 2) {
                    System.out.println("非法操作! 请键入数字编号或者键入正确的范围!");
                    command = SysIn.nextLine();
                }
                com = Integer.parseInt(command);
                if (com == 1) {
                    System.out.println(LoadGame.MNG.listPlts());
                    System.out.println("选择行星，请选择行星前的编号");
                    command = SysIn.nextLine();
                    while (command.isEmpty() || command.matches(".*[^0-9].*") ||
                            Integer.parseInt(command) < 0 || Integer.parseInt(command) >= LoadGame.MNG.getAmountsPlts()) {
                        System.out.println("非法操作! 请键入数字编号或者键入正确的范围!");
                        command = SysIn.nextLine();
                    }
                    LoadGame.MNG.setPlt(Integer.parseInt(command));
                } else {
                    LoadGame.MNG.initPlt();
                }

                LoadGame.MNG.setPlt(Integer.parseInt(command));
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

        do {
            System.out.print(opInfo);
            command = SysIn.nextLine();
            boolean isOptSuccess = false;

            // 判断指令是否处在1至11的范围内
            while (command.isEmpty() || command.matches(".*[^0-9].*") ||
                    Integer.parseInt(command) > 11 || Integer.parseInt(command) < 1) {
                System.out.println("非法操作! 请键入数字，范围为 1-11!");
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
                    isOptSuccess = exec.setRules();
                    break;
                case 11:
                    isOptSuccess = true;
                    break;
                default: break;
            }

            if (isOptSuccess) {
                System.out.println("操作成功");
            } else {
                System.out.println("操作失败");
            }

        } while (!command.matches("11"));

        System.out.println("程序正在退出中，请勿关闭程序");
        if (QuitGame.getQg().store()) {
            System.out.println("已保存文件!");
        } else {
            System.out.println("保存文件出错!");
        }

        System.exit(0);
    }
}
