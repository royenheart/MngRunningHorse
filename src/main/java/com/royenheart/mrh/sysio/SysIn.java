package com.royenheart.mrh.sysio;

import java.util.Scanner;

/**
 * 获取命令行输入
 * @author RoyenHeart
 */
public class SysIn {

    private static final Scanner SYS_IN = new Scanner(System.in);
    private SysIn(){}
    public static String nextLine() {
        return SYS_IN.nextLine();
    }

}
