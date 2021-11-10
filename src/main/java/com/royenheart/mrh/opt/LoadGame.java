package com.royenheart.mrh.opt;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.royenheart.mrh.universe.Planet;
import com.royenheart.mrh.universe.Universe;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 游戏载入操作
 * <p>
 *     由LoadGame统一管理参数检测、mng宇宙
 *     当结束游戏时将行星信息传达给QuitGame，托付其完成数据的保存（JSON文件的形式）
 * </p>
 *
 * @author RoyenHeart
 */
public class LoadGame {

    // 单例设计模式，一次游戏只允许一个LoadGame
    private static final LoadGame LD = new LoadGame();
    private LoadGame() {}
    public static LoadGame getLd() {
        return LD;
    }

    public static final Universe MNG = Universe.getMng();
    public static final CheckParam CP = CheckParam.getCp();

    /**
     * 初始化已有的mng宇宙数据，包括所有的行星，国家，卫星
     * <p>
     *     同时进行行星选择，删除
     * </p>
     */
    public void initial() throws IOException {
        Gson gson = new Gson();

        String path = "src/main/resources/planets";
        File file = new File(path);
        File[] fs = file.listFiles();

        assert fs != null;
        // 遍历目录下的json文件
        for (File f : fs) {
            if (f.isFile() && f.getName().matches(".*.json")) {
                Path fp = f.toPath();
                Reader reader = Files.newBufferedReader(fp, StandardCharsets.UTF_8);
                try {
                    Planet plt = gson.fromJson(reader, Planet.class);
                    MNG.addPlt(plt);
                } catch (JsonSyntaxException e) {
                    System.out.println(f.getName() + ":json格式错误，已跳过" );
                }
            }
        }
    }

}
