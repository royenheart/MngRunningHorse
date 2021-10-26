package com.royenheart.mrh.opt;

import com.google.gson.*;
import com.royenheart.mrh.universe.Planet;

import java.io.File;
import java.io.FileReader;

/**
 * 游戏载入操作
 * 载入、生成、删除行星
 *
 * @author RoyenHeart
 */
public class LoadGame {

    /**
     * 初始化开始界面
     * 载入资源文件
     * 生成选项：载入行星、生成行星、编辑行星
     * 行星卫星数据保存至resources/data的json文件内
     * 以行星名字来命名
     *
     * @return 是否初始化成功
     */
    public boolean initial() {
        File pltSrc = new File(".");
        System.out.println(pltSrc.getAbsoluteFile());
        JsonObject a = new JsonObject();
        return true;
    }

}
