package com.royenheart.mrh.gamedata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royenheart.mrh.existence.Planet;
import com.royenheart.mrh.existence.Universe;
import com.royenheart.mrh.sysio.SysOutErr;
import com.royenheart.mrh.sysio.SysOutTip;

import java.io.IOException;
import java.io.PrintStream;

/**
 * 退出游戏，保存游戏内容
 * <P>
 *     负责将宇宙中的更改的数据（游戏的行星）保存
 *     行星状态保存至本地，待下次游戏时恢复
 * </P>
 * @author RoyenHeart
 */
public class DataStore {

    private final SysOutTip tip;
    private final SysOutErr err;

    // 单例设计模式，一次游戏只允许一个DataStore

    private static final DataStore QG = new DataStore();
    private DataStore() {
        tip = new SysOutTip();
        err = new SysOutErr();
    }
    public static DataStore getQg() {
        return QG;
    }

    /**
     * 保存当前游玩的行星信息
     */
    public void store() {
        Planet storePlt = Universe.getMng().getPlt();
        String fileName = "resources/planets/" + storePlt.getName() + ".json";

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .setPrettyPrinting()
                                     .create();

        try {
            PrintStream writer = new PrintStream(fileName);
            gson.toJson(storePlt, Planet.class, writer);
            tip.print("保存成功!\n已保存至"+fileName);
        } catch (IOException e) {
            err.print("保存失败!请检查保存目录是否正确，当前行星数据将暂时打印至终端", e);
            gson.toJson(storePlt, Planet.class, System.out);
        }
    }

    /**
     * 保存指定行星对象的信息
     * @param newSat 指定的行星对象
     * @return 是否保存成功
     */
    public boolean store(Planet newSat) {
        String fileName = "resources/planets/" + newSat.getName() + ".json";

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .setPrettyPrinting()
                                     .create();

        try {
            PrintStream writer = new PrintStream(fileName);
            gson.toJson(newSat, Planet.class, writer);
            tip.print("指定行星保存成功!\n已保存至"+fileName);
        } catch (IOException e) {
            err.print("指定行星保存失败!请检查保存目录是否正确，当前行星数据将暂时打印至终端", e);
            gson.toJson(newSat, Planet.class, System.out);
            return false;
        }

        return true;
    }

}
