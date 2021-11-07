package com.royenheart.mrh.opt;

import com.google.gson.JsonObject;
import com.royenheart.mrh.universe.Universe;

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
    public LoadGame getLd() {
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
    public void initial() {
        JsonObject file = new JsonObject();
    }

}
