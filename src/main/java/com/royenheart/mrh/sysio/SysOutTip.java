package com.royenheart.mrh.sysio;

/**
 * 提示信息（参数限制、操作结果状态）
 */
public class SysOutTip extends BaseSysOut {

    public SysOutTip() {}

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void print(StringBuffer message) {
        System.out.println(message);
    }

    /**
     * 生成参数的范围限制
     * @param low 最小值（int）
     * @param high 最大值（int）
     * @return 生成的范围限制字符串
     */
    public String rangeLimitGen(int low, int high) {
        return String.format(
                "(大小范围:[%d-%d])",
                low, high
        );
    }

    /**
     * 生成参数的范围限制
     * @param low 最小值（double）
     * @param high 最大值（double）
     * @return 生成的范围限制字符串
     */
    public String rangeLimitGen(double low, double high) {
        return String.format(
                "(大小范围:[%.2f-%.2f])",
                low, high
        );
    }

}
