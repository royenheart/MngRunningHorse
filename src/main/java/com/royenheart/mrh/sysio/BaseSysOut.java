package com.royenheart.mrh.sysio;

/**
 * 输出抽象类
 * <p>
 *     由前序的对象处理好信息后，将最终的信息交给不同级别的输出对象处理
 * </p>
 * @author RoyenHeart
 */
public abstract class BaseSysOut {

    /**
     * 每个输出类型得有相应的输出信息方法
     * @param message 输出信息
     */
    abstract public void print(String message);

    /**
     * 重载，支持StringBuffer类的参数输入
     * @param message 输出信息
     */
    abstract public void print(StringBuffer message);

}
