package com.royenheart;

import java.util.Scanner;

/**
 * Get System input
 */
public class SysIn {

    private static Scanner SysIn = new Scanner(System.in);

    private SysIn(){}

    public static String nextLine() {
        return SysIn.nextLine();
    }

    public static String next() {
        return SysIn.next();
    }

}
