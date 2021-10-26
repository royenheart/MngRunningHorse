package com.royenheart;

import com.royenheart.mrh.opt.BaseMenu;
import com.royenheart.mrh.opt.InGame;
import com.royenheart.mrh.opt.QuitGame;

/**
 * 命令行方式运行飞马卫星管理程序
 *
 * @author RoyenHeart
 */
public class MRHCli {
    public static void main(String[] args) {

        // 载入界面
        BaseMenu startMenu = new InGame();



        // 离开界面
        BaseMenu quitMenu = new QuitGame();

    }
}
