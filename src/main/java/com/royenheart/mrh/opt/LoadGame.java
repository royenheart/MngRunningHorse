package com.royenheart.mrh.opt;

import com.google.gson.Gson;
import com.royenheart.mrh.universe.Country;
import com.royenheart.mrh.universe.Planet;
import com.royenheart.mrh.universe.Satellite;
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

    private static LoadGame ld = new LoadGame();
    private LoadGame() {}
    public static LoadGame getLd() {
        return ld;
    }

    public static Universe mng = Universe.getMng();
    public static CheckParam cp = CheckParam.getCp();

    /**
     * 初始化已有的mng宇宙数据，包括所有的行星，国家，卫星
     * <p>
     *     同时进行行星选择，删除
     * </p>
     */
    public void initial() throws IOException {
        Gson gson = new Gson();

        String fileName = "src/main/resources/planets/RunningHorse.json";
        Path path = new File(fileName).toPath();

        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

        Planet plt = gson.fromJson(reader, Planet.class);

        /*
          设置国家所属行星和卫星所属国家
         */
        for (Country cty : plt.ctys) {
            cty.setBelong(plt);
            for (Satellite e : cty.sats) {
                e.setCty(cty);
            }
        }

        mng.addPlt(plt);
    }

}
