package com.royenheart.mrh.opt;

import com.royenheart.SysIn;
import com.royenheart.mrh.universe.Country;
import com.royenheart.mrh.universe.Satellite;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;

/**
 * 宇宙操作
 *
 * @author RoyenHeart
 */
public class GamingOpt {

    /**
     * 卫星查找规则集开启情况
     */
    private BitSet rulesOn = new BitSet();

    /**
     * 卫星查找规则集索引
     */
    private Hashtable<String, Integer> rules = new Hashtable<>();

    public GamingOpt() {
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
     * 列出当前查找规则集
     */
    public void listRules() {
        System.out.printf(
                "当前查找规则\n名称查询: %2s\ncosparid查询: %2s\n模糊查询: %2s\n忽略大小写: %2s\n%n",
                (getRulesOn("name"))?"是":"否",
                (getRulesOn("cosparid"))?"是":"否",
                (getRulesOn("fuzzySearchOn"))?"是":"否",
                (getRulesOn("caseLockOn"))?"是":"否"
        );
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
                keyVal = SysIn.nextLine();
                err = LoadGame.cp.checkSat(key, keyVal);
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
                    for (Country cty : LoadGame.mng.getPlt().ctys) {
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

        // 添加卫星至行星

        Satellite newSat = new Satellite(name, cosparid, dis, value, used, belongCty);

        assert belongCty != null;
        belongCty.sats.add(newSat);

        return true;
    }

    /**
     * 根据关键字查找所有符合条件的卫星
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
    private Satellite[] findSatList(String param) {
        /*
          查找到的卫星列表
         */
        ArrayList<Satellite> sats = new ArrayList<>();

        /*
          cosparid查找和name查找只留一个
         */
        while (getRulesOn("name") == getRulesOn("cosparid")) {
            String open;
            if (getRulesOn("name") && getRulesOn("cosparid")) {
                System.out.print("检测到名称查找和cosparid查找同时开启，请保留一个（name/cosparid）: ");
            } else {
                System.out.print("检测到名称查找和cosparid查找全部关闭，请开启一个（name/cosparid）: ");
            }
            while (!(open = SysIn.nextLine()).matches("(^name$)|(^cosparid$)")) {
                System.out.print("参数错误！，请填写指定查找参数: ");
            }
            setRulesOn("name", false);
            setRulesOn("cosparid", false);
            setRulesOn(open, true);
        }

        for (Country cty : LoadGame.mng.getPlt().ctys) {
            for (Satellite sat : cty.sats) {
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
        }

        // 注意toArray是浅拷贝，之后考虑重构防止破坏封装性
        return sats.toArray(new Satellite[0]);
    }

    /**
     * 调用findSatList，并选取指定的卫星
     *
     * @return 选取的单个卫星
     */
    private Satellite findSat() {
        String findParam = SysIn.nextLine();
        String tmp;
        Satellite[] list;
        Satellite sat;
        StringBuffer info = new StringBuffer();

        list = findSatList(findParam);
        while (list.length == 0) {
            System.out.println("未查找到指定的卫星，请重新输入查找信息");
            findParam = SysIn.nextLine();
            list = findSatList(findParam);
        }

        listSat(info, list);
        System.out.println(info);

        if (list.length > 1) {
            System.out.println("请输入卫星前的序号选择卫星");
            tmp = SysIn.nextLine();
            while (tmp.isEmpty() || tmp.matches(".*[^0-9].*") || Integer.parseInt(tmp) > list.length || Integer.parseInt(tmp) < 1) {
                System.out.println("Illegal number! Please insert a number range from 1-"+list.length+"!");
                tmp = SysIn.nextLine();
            }
            sat = list[Integer.parseInt(tmp) - 1];
        } else {
            sat = list[0];
        }

        System.out.println("已选择");
        System.out.println(sat.toString());

        return sat;
    }

    /**
     * 打印出当前行星信息和全部已有的卫星列表
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

        info.append("The name: ").append(LoadGame.mng.getPlt().getName())
            .append("The size: ").append(LoadGame.mng.getPlt().getSize())
            .append("The background: \n").append(LoadGame.mng.getPlt().getDesc());
    }

    /**
     * 打印卫星列表，全部的已有卫星
     *
     * @param info 添加卫星信息
     */
    public void listSat(StringBuffer info) {
        info.append("----------------The Satellites-------------------\n")
            .append(
                    String.format(
                            "序号%10s,%10s,%10s,%10s,%10s,%10s\n",
                            "卫星名字", "卫星轨道半径", "卫星轨道价值", "卫星cosparid", "卫星所属城市", "是否可用"
                    )
            );

        int i = 0;
        for (Country cty : LoadGame.mng.getPlt().ctys) {
            for (Satellite sat : cty.sats) {
                info.append(++i).append(sat.toString());
            }
        }

        if (i == 0) {
            info.append("未查找到指定的卫星");
        }
    }

    /**
     * 打印卫星列表，打印传入的卫星列表
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

        int i = 0;
        for (Satellite sat : sats) {
            info.append(++i).append(sat.toString());
        }

    }

    /**
     * 查找卫星并根据传入的卫星列表打印出信息
     * <p>
     *     填入查找的参数
     *     调用findSat方法返回卫星列表
     *     注意打印卫星信息
     * </p>
     *
     * @return 是否正确查找
     */
    public boolean SatList() {
        StringBuffer info = new StringBuffer();

        listRules();

        System.out.print("现在开始查找卫星，请输入查找的关键字: ");

        listSat(info, findSatList(SysIn.nextLine()));
        System.out.println(info);

        return true;
    }

    /**
     * 编辑修改卫星信息
     *
     * @return 是否修改成功
     */
    public boolean editSat() {
        Satellite sat;
        String tmp;
        System.out.println("现在开始修改卫星信息");
        System.out.println("请注意只能修改卫星的名字，请先根据关键词查找卫星:");

        listRules();

        sat = findSat();

        String err = "";

        do {
            System.out.println(err + "需要修改成什么名字？");
            tmp = SysIn.nextLine();
            err = LoadGame.cp.checkSatName(tmp);
        } while (err.contains("err:"));

        System.out.println("卫星" + sat.getCosparid() + ":" + sat.getName() + "已被更改为");
        System.out.println(sat.getCosparid() + ":" + tmp);
        sat.setName(tmp);

        return true;
    }

    /**
     * 删除卫星
     *
     * @return 是否删除成功
     */
    public boolean delSat() {
        Satellite sat;
        System.out.println("现在开始删除卫星，请先查找卫星");
        System.out.print("开始查找卫星，请输入查找的关键字: ");

        listRules();
        sat = findSat();

        System.out.println("卫星" + sat.getCosparid() + ":" + sat.getName() + "已被删除");
        sat.getBelongCty().sats.removeIf(e -> e.equals(sat));

        return true;
    }

    /**
     * 查找卫星并根据传入参数启用、封锁卫星
     *
     * @return 是否启用成功
     */
    public boolean activateSat(boolean isOpen) {
        Satellite sat;
        System.out.println("现在开始删除卫星，请先查找卫星");
        System.out.print("开始查找卫星，请输入查找的关键字: ");

        listRules();
        sat = findSat();

        System.out.println("卫星已设置为" + (isOpen?"启用":"禁用"));
        sat.setUsed(isOpen);

        return true;
    }

    /**
     * 查找停运的卫星列表
     *
     * @return 是否查找成功
     */
    public boolean findUsedSat() {
        StringBuffer info = new StringBuffer();
        ArrayList<Satellite> list = new ArrayList<>();

        for (Country cty : LoadGame.mng.getPlt().ctys) {
            for (Satellite e : cty.sats) {
                if (!e.getUsed()) {
                    list.add(e);
                }
            }
        }

        if (list.size() == 0) {
            System.out.println("全部卫星已经启用");
        } else {
            listSat(info, list.toArray(new Satellite[0]));
            System.out.println(info);
        }

        return true;
    }

    /**
     * 添加国家
     *
     * @return 是否添加成功
     */
    public boolean addCountry() {
        String name, code;

        System.out.println("新增国家操作请填写国家名和编号");

        name = SysIn.next();
        code = SysIn.next();

        LoadGame.mng.getPlt().ctys.add(new Country(name, code, LoadGame.mng.getPlt()));

        return true;
    }

}
