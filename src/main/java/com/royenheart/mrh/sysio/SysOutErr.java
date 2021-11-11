package com.royenheart.mrh.sysio;

/**
 * 错误信息
 */
public class SysOutErr extends BaseSysOut {

    public SysOutErr() {

    }

    /**
     * 打印出错误
     * @param message 错误信息
     */
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void print(StringBuffer message) {
        System.out.println(message);
    }

    public void print(String message, Exception e) {
        System.out.println(message);
    }

}
