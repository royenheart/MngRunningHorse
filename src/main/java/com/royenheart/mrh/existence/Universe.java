package com.royenheart.mrh.existence;

import com.royenheart.mrh.gamedata.DataStore;
import com.royenheart.mrh.operation.CheckParam;
import com.royenheart.mrh.sysio.SysIn;
import com.royenheart.mrh.sysio.SysOutErr;
import com.royenheart.mrh.sysio.SysOutMain;
import com.royenheart.mrh.sysio.SysOutTip;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * MNG宇宙，宇宙下属多个行星，一次游戏只能有一个宇宙
 * @author RoyenHeart
 */
public class Universe {

    private final SysOutMain out;
    private final SysOutTip tip;
    private final SysOutErr err;

    // 单例设计模式，只允许生成一个宇宙(元宇宙是吧)

    private static final Universe MNG = new Universe();
    private Universe() {
        out = new SysOutMain();
        tip = new SysOutTip();
        err = new SysOutErr();
    }
    public static Universe getMng() {
        return MNG;
    }

    // 宇宙名，由MNG跨宇宙公司管理

    public static final String NAME = "MNG";

    /**
     * 宇宙所有行星列表
     */
    private final ArrayList<Planet> plts = new ArrayList<>();
    private Planet currPlay;

    public static String getName() {
        return NAME;
    }

    /**
     * 打印出当前行星列表
     * @return 行星列表字符串，带序号
     */
    public String listPlts() {

        StringBuilder lists = new StringBuilder();

        lists.append("@~~~行星列表~~~@\n\n");
        for (Planet plt : this.plts) {
            lists.append(String.format("行星序号: %d\n",plts.indexOf(plt)));
            lists.append(String.format(
                    "%s\n",
                    plt
            ));
        }

        lists.append("@~~~行星列表~~~@\n");
        return lists.toString();
    }

    /**
     * 返回行星列表
     * @return 行星列表
     */
    public ArrayList<Planet> getPlts() {
        return plts;
    }

    /**
     * 设置当前游戏的行星
     * @param index 设置的行星在ArrayList中的索引
     */
    public void setPlt(int index) {
        this.currPlay = plts.get(index);
    }

    /**
     * 生成新的行星
     * @return 生成情况
     */
    public boolean initPlt() {
        CheckParam cp = new CheckParam();
        String name;
        String desc;

        int size;
        out.print("现在开始生成行星，请指定参数");

        try {
            Method useWhat = CheckParam.class.getMethod("checkPltName", String.class);
            name = cp.checkParamMethod("请输入行星名字",
                    tip.rangeLimitGen(Planet.MIN_NAME_SIZE, Planet.MAX_NAME_SIZE),
                    useWhat, cp);
            out.print("请输入行星简介,可为空: ");
            desc = SysIn.nextLine();
            useWhat = CheckParam.class.getMethod("checkPltSize", String.class);
            size = Integer.parseInt(
                    cp.checkParamMethod("请输入卫星尺寸",
                            tip.rangeLimitGen(Planet.MIN_SIZE, Planet.MAX_SIZE),
                            useWhat, cp)
            );
        } catch (NoSuchMethodException e) {
            err.print("检测方法出错，请检查是否正确引用", e);
            return false;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
            return false;
        }

        Planet newPlt = new Planet(name, desc, size, new ArrayList<>());
        plts.add(newPlt);
        tip.print("行星生成成功");
        out.print(newPlt.toString());

        // 保存新行星
        if (DataStore.getQg().store(newPlt)) {
            tip.print("保存行星成功");
        } else {
            tip.print("保存行星出错!");
        }
        return true;
    }

    /**
     * 获取当前正在游戏的行星
     * @return 正在游戏的行星
     */
    public Planet getPlt() {
        return currPlay;
    }

    /**
     * 添加行星
     * @param newPlt 新的行星
     */
    public void addPlt(Planet newPlt) {
        plts.add(newPlt);
    }

    /**
     * 当前加载成功的行星数量
     * @return 行星数量
     */
    public int getAmountsPlts() {
        return plts.size();
    }

    @Override
    public String toString() {
        return NAME + "元宇宙\n" +
                "行星: " + plts +
                "\n当前游戏行星: " + currPlay;
    }

}