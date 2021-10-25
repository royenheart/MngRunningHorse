package mrh.universe;

/**
 * 卫星对象
 * 包括卫星数据：
 * 卫星所处轨道、卫星cosparid、卫星所属国家、卫星状态
 *
 * 需实现方法：
 * 合法生成卫星、修改卫星状态并同步至行星轨道状态
 *
 * @author RoyenHeart
 */
public class Satellite {

    // 卫星数据

    private int track;
    private String cosparid;
    private Country belongCty;
    //卫星状态，是否在使用
    private boolean used;

    /**
     * 返回卫星信息
     * 包括所处轨道、卫星cosparid、所属国家名称、是否在使用
     *
     * @return
     */
    @Override
    public String toString() {
        String satInfo;
    }

    public int getTrack() {
        return track;
    }

    public String getCosparid() {
        return String.valueOf(cosparid);
    }

    public String getBelongCty() {
        return belongCty.getName();
    }

    public boolean getUsed() {
        return used;
    }
}
