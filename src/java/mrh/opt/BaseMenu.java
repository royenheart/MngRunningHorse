package mrh.opt;

/**
 * 规定载入、游戏中、退出界面的抽象
 *
 * @author RoyenHeart
 */
public abstract class BaseMenu {

    /**
     * 实现界面的显示
     *
     * @param guiOn 是否开启图形界面
     */
    public abstract void show(boolean guiOn);
}
