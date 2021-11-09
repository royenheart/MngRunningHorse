package com.royenheart.mrh.opt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royenheart.mrh.universe.Planet;

import java.io.*;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
    public static QuitGame getQG() {
        return QG;
    }

    /**
     * 保存游戏的行星信息
     */
    public boolean store() {

        Planet storePlt = LoadGame.MNG.getPlt();
        String fileName = "src/main/resources/planets/" + storePlt.getName() + ".json";
        Path path = new File(fileName).toPath();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .setPrettyPrinting()
                                     .create();

        try {
            PrintStream writer = new PrintStream(fileName);
            gson.toJson(storePlt, Planet.class, writer);
        } catch (IOException e) {
            System.out.println("保存文件出错!");
            e.printStackTrace();
            return false;
        }

        System.out.println("已保存文件!");
        return true;
    }

}
