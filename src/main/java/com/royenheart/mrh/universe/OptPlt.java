package com.royenheart.mrh.universe;

import com.royenheart.mrh.opt.CheckParam;

import java.util.*;

/**
 * 生成、载入、保存、操作行星及卫星操作
 *
 * @author RoyenHeart
 */
public class OptPlt {

    /**
     * 当前所选择的行星
     */
    private Planet plt = null;

    /**
     * 检测函数
     */
    private CheckParam cp = null;

    /**
     * 卫星查找规则集开启情况
     */
    private BitSet rulesOn = new BitSet();

    /**
     * 卫星查找规则集索引
     */
    private Hashtable<String, Integer> rules = new Hashtable();

    public OptPlt() {
        // 初始化规则集

        rules.put("name", 0);
        rules.put("cosparid", 1);
        rules.put("caseLockOn", 2);
        rules.put("fuzzySearchOn", 3);
        try {
            // 默认按照名称查询
            rulesOn.set(rules.get("name"));
            rulesOn.clear(rules.get("cosparid"));
            rulesOn.set(rules.get("caseLockOn"));
            rulesOn.set(rules.get("fuzzySearchOn"));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 卫星查找规则集指定规则切换
     *
     * @param param 待查找的规则名
     */
    public void setRulesOn(String param) {
        try {
            rulesOn.set(rules.get(param), !getRulesOn(param));
        } catch (ClassCastException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 卫星查找规则集指定规则指定切换
     *
     * @param param 待查找的规则名
     * @param status 指定开启状态
     */
    public void setRulesOn(String param, boolean status) {
        try {
            rulesOn.set(rules.get(param), status);
        } catch (ClassCastException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 卫星查找规则集查询当前规则开启情况
     *
     * @param param 待查找的规则名
     * @return 返回该规则开启情况
     */
    public boolean getRulesOn(String param) throws
            ClassCastException,NullPointerException,IndexOutOfBoundsException {
        boolean status;
        status = rulesOn.get(rules.get(param));
        return status;
    }

    /**
     * 添加卫星
     *
     * @return 是否添加成功
     */
    public boolean addSatellite() {
        Scanner text = new Scanner(System.in);

        String name = null;
        double dis = 0;
        double value = 0;
        String cosparid = null;
        Country belongCty = null;
        boolean used = false;

        ArrayList<String> params = new ArrayList<>();
        params.add("name");
        params.add("track");
        params.add("value");
        params.add("cosparid");
        params.add("belongCty");
        params.add("used");

        System.out.println("现在开始添加卫星，请添加对应的信息");
        System.out.println("卫星使用状态请填写true、false等真假字符");

        /* 遍历键值对 */
        for (String key : params ) {
            String err = "";
            String keyVal;

            /* 获取合法键值对 */
            do {
                System.out.print( err + "请填写" + key + ": ");
                keyVal = text.nextLine();
                err = cp.checkSat(key, keyVal);
            } while (err.contains("err:"));

            switch (key) {
                case "name":
                    name = keyVal;
                    break;
                case "dis":
                    dis = Double.parseDouble(keyVal);
                    break;
                case "value":
                    value = Double.parseDouble(keyVal);
                    break;
                case "cosparid":
                    cosparid = keyVal;
                    break;
                case "belongCty":
                    for (Country cty : plt.ctys) {
                        if (cty.getCode().equals(keyVal) || cty.getName().equals(keyVal)) {
                            belongCty = cty;
                            break;
                        }
                    }
                    break;
                case "used":
                    used = Boolean.parseBoolean(keyVal);
                    break;
                default:
                    break;
            }
        }

        // 添加卫星和对应轨道至行星

        Track newTrack = new Track(dis, value, used);
        Satellite newSat = new Satellite(name, newTrack, cosparid, belongCty, used, plt);

        plt.sats.add(newSat);
        plt.tracks.add(newTrack);

        return true;
    }

    /**
     * 打印出当前行星信息和卫星列表
     *
     * @return 行星信息和卫星列表
     */
    public String listInfo() {
        StringBuffer info = new StringBuffer();

        info.append("Now shows the lists of planets and the satellites\n");

        listPlt(info);
        listSat(info);

        info.append("---------------------End-------------------------\n");

        return info.toString();
    }

    /**
     * 打印行星信息
     *
     * @param info 添加行星信息
     */
    public void listPlt(StringBuffer info) {
        info.append("-----------------The Planet----------------------\n");

        info.append("The name: ").append(plt.getName())
            .append("The size: ").append(plt.getSize())
            .append("The background: \n").append(plt.getDesc());
    }

    /**
     * 打印卫星列表
     *
     * @param info 添加卫星信息
     */
    public void listSat(StringBuffer info) {
        info.append("----------------The Satellites-------------------\n")
            .append(
                    String.format(
                            "%10s,%10s,%10s,%10s,%10s,%10s\n",
                            "卫星名字", "卫星轨道半径", "卫星轨道价值", "卫星cosparid", "卫星所属城市", "是否可用"
                    )
            );

        for (Satellite sat : plt.sats) {
            info.append(sat.toString());
        }
    }

    /**
     * 打印卫星列表（重载）
     * <p>
     *     根据传入的卫星列表打印
     * </p>
     *
     * @param info 添加卫星信息
     * @param sats 卫星列表
     */
    public void listSat(StringBuffer info, Satellite[] sats) {
        info.append("----------------The Satellites-------------------\n")
            .append(
                    String.format(
                            "%10s,%10s,%10s,%10s,%10s,%10s\n",
                            "卫星名字", "卫星轨道半径", "卫星轨道价值", "卫星cosparid", "卫星所属城市", "是否可用"
                    )
            );

        for (Satellite sat : sats) {
            info.append(sat.toString());
        }
    }

    /**
     * 编辑修改卫星信息
     *
     * @return 是否修改成功
     */
    public boolean editSat() {
        System.out.print("现在开始修改卫星信息，请先输入指定卫星的信息: ");
        Scanner text = new Scanner(System.in);

        String name = text.nextLine();

        return true;
    }

    /**
     * 删除卫星
     *
     * @return 是否删除成功
     */
    public boolean delSat() {
        return true;
    }

    /**
     * 查找卫星：
     * <p>
     *     可以自行开关下列的规则
     *     1. 可开启模糊查找
     *     2. 可开启忽略大小写
     *     3. 可根据名称查找卫星
     *     4. 可根据cosparid查找卫星
     *     其中名称查找和cosparid不能同时开启
     *     private封装方法，阻止对返回的Satellite引用对象数组直接进行操作
     * </p>
     *
     * @param param 查找的参数
     * @return 查找到的卫星对象列表
     */
    private Satellite[] findSat(String param) {
        /*
          查找到的卫星列表
         */
        ArrayList<Satellite> sats = new ArrayList<>();

        /*
          cosparid查找和name查找只留一个
         */
        Scanner text = new Scanner(System.in);
        while (getRulesOn("name") == getRulesOn("cosparid")) {
            String open;
            if (getRulesOn("name") && getRulesOn("cosparid")) {
                System.out.print("检测到名称查找和cosparid查找同时开启，请保留一个（name/cosparid）: ");
            } else {
                System.out.print("检测到名称查找和cosparid查找全部关闭，请开启一个（name/cosparid）: ");
            }
            while (!(open = text.nextLine()).matches("(^name$)|(^cosparid$)")) {
                System.out.print("参数错误！，请填写指定查找参数: ");
            }
            setRulesOn("name", false);
            setRulesOn("cosparid", false);
            setRulesOn(open, true);
        }

        for (Satellite sat : plt.sats) {
            String satName = sat.getName();
            String satCos = sat.getCosparid();
            if (getRulesOn("caseLockOn")) {
                satName = satName.toLowerCase();
                param = param.toLowerCase();
            }
            if (getRulesOn("name")) {
                if (getRulesOn("fuzzySearchOn")) {
                    if (satName.contains(param)) {
                        sats.add(sat);
                    }
                } else {
                    if (satName.equals(param)) {
                        sats.add(sat);
                    }
                }
            }
            if (getRulesOn("cosparid")) {
                if (getRulesOn("fuzzySearchON")) {
                    if (satCos.contains(param)) {
                        sats.add(sat);
                    }
                } else {
                    if (satCos.equals(param)) {
                        sats.add(sat);
                    }
                }
            }
        }

        // 注意toArray是浅拷贝，之后考虑重构防止破坏封装性
        return sats.toArray(new Satellite[0]);
    }

    /**
     * 查找卫星并列出表格
     * <p>
     *     填入查找的参数
     *     调用findSat方法返回卫星列表
     *     注意打印卫星信息
     * </p>
     *
     * @return 是否正确查找
     */
    public boolean findSatList() {
        Scanner text = new Scanner(System.in);
        StringBuffer info = new StringBuffer();

        System.out.printf(
                "当前查找规则\n名称查询: %2s\ncosparid查询: %2s\n模糊查询: %2s\n忽略大小写: %2s\n%n",
                (getRulesOn("name"))?"是":"否",
                (getRulesOn("cosparid"))?"是":"否",
                (getRulesOn("fuzzySearchOn"))?"是":"否",
                (getRulesOn("caseLockOn"))?"是":"否"
        );

        System.out.print("现在开始查找卫星，请输入查找的关键字: ");

        listSat(info, findSat(text.nextLine()));
        System.out.println(info);

        return true;
    }

    /**
     * 根据cosparid查找卫星并根据传入参数启用、封锁卫星
     *
     * @return 是否启用成功
     */
    public boolean activateSat() {
        return false;
    }

    /**
     * 设置当前被操作的行星
     *
     * @param plt 行星对象
     * @return 是否成功
     */
    public boolean setPlt(Planet plt) {
        if (this.plt == null) {
            this.plt = plt;
            cp = new CheckParam(plt);
            return true;
        } else {
            return false;
        }
    }

}