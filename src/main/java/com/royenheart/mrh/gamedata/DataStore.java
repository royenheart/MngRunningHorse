package com.royenheart.mrh.gamedata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royenheart.mrh.existence.Planet;
import com.royenheart.mrh.existence.Universe;

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

    // 单例设计模式，一次游戏只允许一个QuitGame

    private static final DataStore QG = new DataStore();
    private DataStore() {}
    public static DataStore getQg() {
        return QG;
    }

    /**
     * 保存当前游玩的行星信息
     * @return 是否保存成功
     */
    public boolean store() {

        Planet storePlt = Universe.getMng().getPlt();
        String fileName = "src/main/resources/planets/" + storePlt.getName() + ".json";

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .setPrettyPrinting()
                                     .create();

        try {
            PrintStream writer = new PrintStream(fileName);
            gson.toJson(storePlt, Planet.class, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 保存指定行星对象的信息
     * @return 是否保存成功
     */
    public boolean store(Planet newSat) {
        String fileName = "src/main/resources/planets/" + newSat.getName() + ".json";

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .setPrettyPrinting()
                                     .create();

        try {
            PrintStream writer = new PrintStream(fileName);
            gson.toJson(newSat, Planet.class, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
