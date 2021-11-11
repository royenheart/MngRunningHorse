package com.royenheart.mrh.sysio;

/**
 * 打印错误信息
 * @author RoyenHeart
 */
public class SysOutErr extends BaseSysOut {

    public SysOutErr() {}

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

    /**
     * 打印错误以及错误发生的调用栈信息
     * @param message 错误信息
     * @param e 异常对象
     */
    public void print(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }

}
