package com.royenheart.mrh.opt;

import com.google.gson.*;

/**
 * 游戏载入操作
 * 载入、生成、删除行星
 *
 * @author RoyenHeart
 */
public class InGame extends BaseMenu {

    /**
     * 初始化开始界面
     * 载入资源文件
     * 生成选项：载入行星、生成行星、编辑行星
     *
     * @return 是否初始化成功
     */
    public boolean initial() {
        return true;
    }

    /**
     * 选择行星
     *
     * @return 是否选择成功
     */
    public boolean cliChoose() {
        System.out.println("Please choose what planet you want to play");

        return true;
    }

    @Override
    public void show() {

    }


}
