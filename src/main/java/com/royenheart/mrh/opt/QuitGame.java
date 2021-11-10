package com.royenheart.mrh.opt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royenheart.mrh.universe.Planet;

import java.io.IOException;
import java.io.PrintStream;

/**
 * 退出游戏，保存游戏内容
 * <P>
 *     LoadGame将数据传输至此，由QuitGame将数据存储至json文件进行保存
 * </P>
 *
 * @author RoyenHeart
 */
public class QuitGame {

    // 单例设计模式，一次游戏只允许一个QuitGame
    private static final QuitGame QG = new QuitGame();
    private QuitGame() {}
    public static QuitGame getQg() {
        return QG;
    }

    /**
     * 保存当前游玩的行星信息
     */
    public boolean store() {

        Planet storePlt = LoadGame.MNG.getPlt();
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
