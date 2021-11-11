package com.royenheart.mrh.sysio;

/**
 * 主要信息
 */
public class SysOutMain extends BaseSysOut {

    /**
     * 操作提示
     */
    private static final String OPT_COMMAND = "" +
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

    private static final String OPT_PLT = "" +
            "=============================\n" +
            "   1---选择行星\n" +
            "   2---生成新行星\n" +
            "=============================\n" +
            "选择: ";

    public SysOutMain() {

    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void print(StringBuffer message) {
        System.out.println(message);
    }

    /**
     * 打印操作行星提示
     */
    public void printOptPltChoose() {
        System.out.println(OPT_PLT);
    }

    /**
     * 打印选择指令提示
     */
    public void printOptCommand() {
        System.out.println(OPT_COMMAND);
    }

}
