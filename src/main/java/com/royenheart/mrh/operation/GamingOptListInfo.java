package com.royenheart.mrh.operation;

import com.royenheart.mrh.existence.Country;
import com.royenheart.mrh.existence.Satellite;
import com.royenheart.mrh.sysio.SysOutMain;

import java.util.ArrayList;

/**
 * 不同方法返回不同数据至info，info存储打印的信息，再由调用的函数进行处理输出
 * @author RoyenHeart
 */
public class GamingOptListInfo extends GamingOpt {

    private final SysOutMain out;

    public GamingOptListInfo() {
        out = new SysOutMain();
    }

    /**
     * 打印出当前行星信息和全部已有的卫星列表
     * @return 行星信息和卫星列表
     */
    public boolean listInfoAll() {
        StringBuffer info = new StringBuffer();

        info.append("\n现在开始列出当前活动行星信息（包括行星信息、国家信息、卫星信息）\n");

        listPlt(info);
        listCty(info);
        // 卫星的打印交给重载函数listSat(String info)获取info，再打印info
        listSat(info);

        out.print(info);

        return true;
    }

    /**
     * 打印行星信息
     * @param info 添加行星信息
     */
    public void listPlt(StringBuffer info) {
        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~~The Planet~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n");
        info.append(mng.getPlt());
        info.append(
                "@~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~END~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n"
        );
    }

    /**
     * 打印国家信息
     * @param info 添加国家信息
     */
    public void listCty(StringBuffer info) {
        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~~The Country~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n");
        if (mng.getPlt().getAmountsCty() <= 0) {
            info.append("当前不存在国家，请先创建\n");
        } else {
            for (Country cty : mng.getPlt().getCtys()) {
                info.append(cty);
            }
        }
        info.append(
                "@~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~END~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n"
        );
    }

    /**
     * 打印卫星列表，全部的已有卫星
     * @param info 添加卫星信息
     */
    public void listSat(StringBuffer info) {
        int i = 0;

        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~The Satellites~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n")
            .append(
                    String.format(
                            "%-4s%-13s%-12s%-12s%-11s%-12s%-7s\n",
                            "No.", "Name", "Distance", "Value", "Cosparid", "Country", "IsUsed"
                    )
            );
        if (mng.getPlt().getAmountsSat() <= 0) {
            info.append("当前不存在卫星\n");
        } else {
            for (Country cty : mng.getPlt().getCtys()) {
                for (Satellite sat : cty.getSats()) {
                    info.append(String.format("%-4s", ++i)).append(sat.toString());
                }
            }
        }
        info.append(
                "@~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~END~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n"
        );
    }

    /**
     * 打印卫星列表，打印传入的卫星列表
     * <p>
     *     根据传入的卫星列表打印
     * </p>
     * @param info 添加卫星信息
     * @param sats 卫星列表
     */
    public void listSat(StringBuffer info, ArrayList<Satellite> sats) {
        int i = 0;

        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~~The Satellites~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n")
            .append(
                    String.format(
                            "%-4s%-13s%-12s%-12s%-11s%-12s%-7s\n",
                            "No.", "Name", "Distance", "Value", "Cosparid", "Country", "IsUsed"
                    )
            );
        if (sats == null) {
            info.append("当前不存在卫星\n");
        } else {
            if (sats.isEmpty()) {
                info.append("未查找到卫星\n");
            } else {
                for (Satellite sat : sats) {
                    info.append(String.format("%-4s", ++i)).append(sat.toString());
                }
            }
        }
        info.append(
                "@~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~END~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n"
        );

    }

}
