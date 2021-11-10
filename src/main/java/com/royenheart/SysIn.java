package com.royenheart;

import java.util.Scanner;

/**
 * 获取命令行输入
 * @author RoyenHeart
 */
public class SysIn {

    private static final Scanner SysIn = new Scanner(System.in);
    private SysIn(){}
    public static String nextLine() {
        return SysIn.nextLine();
    }

}
