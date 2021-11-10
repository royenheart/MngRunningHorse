package com.royenheart.mrh.opt;

import com.royenheart.SysIn;
import com.royenheart.mrh.universe.Country;
import com.royenheart.mrh.universe.Satellite;
import com.sun.istack.internal.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;

/**
 * 宇宙操作
 *
 * @author RoyenHeart
 */
public class GamingOpt {

    // 单例设计模式，只允许一个GamingOpt类操作
    private static final GamingOpt EXEC = new GamingOpt();
    private GamingOpt() {
        // 初始化规则集
        rules.put("name", 0);
        rules.put("cosparid", 1);
        rules.put("caseLockOn", 2);
        rules.put("fuzzySearchOn", 3);
        rulesOn.set(rules.get("name"), true);
        rulesOn.set(rules.get("cosparid"), false);
        rulesOn.set(rules.get("caseLockOn"));
        rulesOn.set(rules.get("fuzzySearchOn"));
    }
    public static GamingOpt getExec() {
        return EXEC;
    }

    /**
     * 卫星查找规则集开启情况
     */
    private final BitSet rulesOn = new BitSet();

    /**
     * 卫星查找规则集索引
     * <p>
     *     索引单词 -> 对应规则集位置
     * </p>
     */
    private final Hashtable<String, Integer> rules = new Hashtable<>();

    /**
     * 配置查找规则集信息
     *
     * @return 是否配置成功
     */
    public boolean setRules() {
        String command;
        String err;

        System.out.println("\n现在开始配置卫星查找的规则集");
        listRules();

        for (String s : rules.keySet()) {
            err = "";

            do {
                System.out.print(err + "请填写" + s + ": ");
                command = SysIn.nextLine();
                err = LoadGame.CP.checkTrueFalse(command);
            } while (err.contains("err:"));

            setRulesOn(s, LoadGame.CP.getTrueFalseFromIn(command));
        }

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

        return true;
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
        System.out.println("\n当前查找规则");
        for (String key : rules.keySet()) {
            System.out.println(key + ":" + (getRulesOn(key) ? "是" : "否"));
        }
        System.out.println();
    }

    /**
     * 卫星查找规则集查询当前规则开启情况
     *
     * @param param 待查找的规则名
     * @return 返回该规则开启情况
     */
    public boolean getRulesOn(String param) {
        return rulesOn.get(rules.get(param));
    }

    /**
     * 添加卫星
     *
     * @return 是否添加成功
     */
    public boolean addSatellite() {
        String err;
        String name = null;
        double dis = 0;
        /* 待添加的卫星轨道价值 */
        double disValue = 0;
        String cosparid = null;
        Country belongCty = null;
        boolean used = false;
        Hashtable<Method, String> params = new Hashtable<>();
        StringBuffer info = new StringBuffer();

        System.out.println("\n当前已有国家和卫星");
        listCty(info);
        listSat(info);
        System.out.println(info);

        if (LoadGame.MNG.getPlt().getAmountsCty() <= 0) {
            System.out.println("当前不存在任何国家，无法执行添加卫星操作，请建立起一个国家后再来");
            return false;
        }

        try {
            params.put(CheckParam.class.getMethod("checkSatName", String.class), "卫星名字");
            params.put(CheckParam.class.getMethod("checkSatDis", String.class), "卫星轨道半径");
            params.put(CheckParam.class.getMethod("checkSatTruckValue", String.class), "卫星轨道价值");
            params.put(CheckParam.class.getMethod("checkSatCos", String.class), "卫星cosparid");
            params.put(CheckParam.class.getMethod("checkSatUsed", String.class), "卫星使用情况");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println("现在开始添加卫星，请添加对应的信息");
        System.out.println("卫星使用状态请填写表示真假的单词");

        /*
        遍历param获取方法
         */
        for (Method method : params.keySet()) {
            String keyVal;

            err = "";
            /* 获取合法参数 */
            do {
                System.out.print(err + "请填写" + params.get(method) + ": ");
                keyVal = SysIn.nextLine();
                try {
                    err = (String) method.invoke(LoadGame.CP, keyVal);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } while (err.contains("err:"));

            switch (params.get(method)) {
                case "卫星名字":
                    name = keyVal;
                    break;
                case "卫星轨道半径":
                    dis = Double.parseDouble(keyVal);
                    break;
                case "卫星轨道价值":
                    disValue = Double.parseDouble(keyVal);
                    break;
                case "卫星cosparid":
                    cosparid = keyVal.toUpperCase();
                    break;
                case "卫星使用情况":
                    used = LoadGame.CP.getTrueFalseFromIn(keyVal);
                    break;
                default:
                    System.out.println("未知键值对，操作中断");
                    return false;
            }
        }

        // 添加卫星至行星

        Satellite newSat = new Satellite(name, cosparid, dis, disValue, used);

        // 根据cosparid获取所属国家
        for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
            if (cty.getCode().equals(cosparid.substring(0,2))) {
                belongCty = cty;
                break;
            }
        }

        belongCty.getSats().add(newSat);
        System.out.println("卫星已添加\n" + newSat);

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

        if (LoadGame.MNG.getPlt().getAmountsSat() <= 0) {
            return null;
        } else if (param.isEmpty()) {
            return sats.toArray(new Satellite[0]);
        }
        for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
            for (Satellite sat : cty.getSats()) {
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
                    if (getRulesOn("fuzzySearchOn")) {
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

        return sats.toArray(new Satellite[0]);
    }

    /**
     * 调用findSatList，并选取指定的卫星
     *
     * @return 选取的单个卫星
     */
    private Satellite findSat() {
        String findParam;
        String command;
        String err;
        Satellite[] list;
        Satellite sat;
        StringBuffer info = new StringBuffer();

        if (LoadGame.MNG.getPlt().getAmountsSat() <= 0) {
            System.out.println("当前不存在卫星");
            return null;
        }
        findParam = SysIn.nextLine();
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
            command = SysIn.nextLine();
            while (command.isEmpty() || command.matches(".*[^0-9].*") || Integer.parseInt(command) > list.length || Integer.parseInt(command) < 1) {
                System.out.println("错误序号! 请键入范围在 1-"+list.length+"的数字!");
                command = SysIn.nextLine();
            }
            sat = list[Integer.parseInt(command) - 1];
        } else {
            err = "";
            do {
                System.out.println(err + "确认选择该卫星？");
                command = SysIn.nextLine();
                err = LoadGame.CP.checkTrueFalse(command);
            } while (err.contains("err:"));
            if (LoadGame.CP.getTrueFalseFromIn(command)) {
                sat = list[0];
            } else {
                return null;
            }
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
    public boolean listInfo() {
        StringBuffer info = new StringBuffer();

        info.append("\n现在开始列出当前活动行星信息（包括行星信息、国家信息、卫星信息）\n");

        listPlt(info);
        listCty(info);
        listSat(info);

        System.out.println(info);

        return true;
    }

    /**
     * 打印行星信息
     *
     * @param info 添加行星信息
     */
    public void listPlt(StringBuffer info) {
        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~~The Planet~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n");

        info.append(LoadGame.MNG.getPlt());
    }

    /**
     * 打印国家信息
     *
     * @param info 添加国家信息
     */
    public void listCty(StringBuffer info) {
        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~~The Country~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n");

        for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
            info.append(cty);
        }

        if (LoadGame.MNG.getPlt().getAmountsCty() <= 0) {
            info.append("当前不存在任何国家，请先创建\n");
        }

    }

    /**
     * 打印卫星列表，全部的已有卫星
     *
     * @param info 添加卫星信息
     */
    public boolean listSat(StringBuffer info) {
        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~The Satellites~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n")
            .append(
                    String.format(
                            "%-4s%-13s%-12s%-12s%-11s%-12s%-7s\n",
                            "No.", "Name", "Distance", "Value", "Cosparid", "Country", "IsUsed"
                    )
            );

        if (LoadGame.MNG.getPlt().getAmountsSat() <= 0) {
            info.append("当前不存在卫星\n");
            return false;
        }

        int i = 0;
        for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
            for (Satellite sat : cty.getSats()) {
                info.append(String.format("%-4s", ++i)).append(sat.toString());
            }
        }

        info.append(
                "@~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~END~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n"
        );

        return true;
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
    public boolean listSat(StringBuffer info, Satellite[] sats) {
        info.append("@~~~~~~~~~~~~~~~~~~~~~~~~~~~~The Satellites~~~~~~~~~~~~~~~~~~~~~~~~~~~@\n")
            .append(
                    String.format(
                            "%-4s%-13s%-12s%-12s%-11s%-12s%-7s\n",
                            "No.", "Name", "Distance", "Value", "Cosparid", "Country", "IsUsed"
                    )
            );

        if (sats == null) {
            info.append("不存在卫星\n");
            return false;
        }

        int i = 0;
        for (Satellite sat : sats) {
            info.append(String.format("%-4s", ++i)).append(sat.toString());
        }

        if (i == 0) {
            info.append("当前列表没有指定卫星\n");
        }

        return true;

    }

    /**
     * 查找卫星打印出信息
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

        System.out.println("现在开始查找卫星，请输入查找的关键字");

        if (LoadGame.MNG.getPlt().getAmountsSat() <= 0) {
            System.out.println("不存在卫星");
            return false;
        }

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
        String command;
        String message = "";
        String err;
        Method check = null;
        Method set = null;
        System.out.println("\n现在开始修改卫星信息");
        System.out.println("可修改名字、轨道价值、轨道半径、cosparid，请先根据关键词查找卫星");

        listRules();

        sat = findSat();

        if (sat == null) {
            System.out.println("编辑修改卫星操作中断");
            return false;
        }

        System.out.printf(
                "需要修改什么参数(选择参数前的序号)？\n1.%s\n2.%s\n",
                "卫星名字","轨道价值"
        );

        command = SysIn.nextLine();
        while (command.isEmpty() || command.matches(".*[^0-9].*") ||
                Integer.parseInt(command) < 1 || Integer.parseInt(command) > 2) {
            System.out.println("非法操作! 请键入数字编号或者键入正确的范围!");
            command = SysIn.nextLine();
        }

        try {
            switch (Integer.parseInt(command)) {
                case 1:
                    check = CheckParam.class.getMethod("checkSatName", String.class);
                    set = Satellite.class.getMethod("setName", String.class);
                    message = "需要修改为什么名字？";
                    break;
                case 2:
                    check = CheckParam.class.getMethod("checkSatTruckValue", String.class);
                    set = Satellite.class.getMethod("setDisValue", String.class);
                    message = "轨道价值修改为多少？";
                    break;
                default:
                    System.out.println("序号范围超出，操作中断");
                    return false;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        err = "";
        do {
            System.out.println(err + message);
            command = SysIn.nextLine();
            try {
                err = (String) check.invoke(LoadGame.CP, command);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } while (err.contains("err:"));

        System.out.println("卫星\n" + sat + "已被更改为");
        try {
            set.invoke(sat,  command);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(sat);


        return true;
    }

    /**
     * 删除卫星
     *
     * @return 是否删除成功
     */
    public boolean delSat() {
        Satellite sat;
        String command;
        System.out.println("\n现在开始删除卫星，请先查找卫星");
        System.out.println("开始查找卫星，请输入查找的关键字");

        listRules();
        sat = findSat();

        if (sat == null) {
            System.out.println("删除卫星操作中断");
            return false;
        }

        System.out.print("是否删除卫星？:");
        command = SysIn.nextLine();
        while (command.isEmpty() | LoadGame.CP.checkTrueFalse(command).contains("err:")) {
            System.out.println("错误参数，请重新输入");
            command = SysIn.nextLine();
        }

        if (LoadGame.CP.getTrueFalseFromIn(command)) {
            sat.inWhichCountry().getSats().removeIf(e -> e.equals(sat));
            System.out.println("卫星" + sat.getCosparid() + ":" + sat.getName() + "已被删除");
            return true;
        } else {
            System.out.println("删除卫星操作中断");
            return false;
        }
    }

    /**
     * 查找卫星并根据传入参数启用、封锁卫星
     *
     * @return 是否启用成功
     */
    public boolean activateSat() {
        Satellite sat;
        String command;
        System.out.println("\n现在开始启用、封锁卫星，请先查找卫星");
        System.out.print("开始查找卫星，请输入查找的关键字: ");

        listRules();
        sat = findSat();

        if (sat == null) {
            System.out.println("封锁、启用卫星操作中断");
            return false;
        }

        System.out.printf("是否%s卫星？:",sat.getUsed()?"封锁":"启用");
        command = SysIn.nextLine();
        while (command.isEmpty() | LoadGame.CP.checkTrueFalse(command).contains("err:")) {
            System.out.println("错误参数，请重新输入");
            command = SysIn.nextLine();
        }

        if (!LoadGame.CP.getTrueFalseFromIn(command)) {
            System.out.println("未做任何改变");
        } else {
            System.out.println("卫星已设置为" + (sat.getUsed()?"封锁":"启用"));
            sat.setUsed();
        }
        System.out.println("选中行星当前状态:\n" + sat);

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

        if (LoadGame.MNG.getPlt().getAmountsSat() <= 0) {
            System.out.println("\n当前不存在卫星");
            return false;
        }
        for (Country cty : LoadGame.MNG.getPlt().getCtys()) {
            for (Satellite e : cty.getSats()) {
                if (!e.getUsed()) {
                    list.add(e);
                }
            }
        }

        if (list.size() == 0) {
            System.out.println("全部卫星已启用");
        } else {
            listSat(info, list.toArray(new Satellite[0]));
            System.out.println("\n" + info);
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
        String err = "";

        System.out.println("\n现在开始新增国家操作");

        do {
            System.out.println( err + "请填写国家名和编号（分行填写）: ");
            name = SysIn.nextLine();
            code = SysIn.nextLine();
            err = LoadGame.CP.checkCtyAdd(name, code);
        } while (err.contains("err:"));

        Country newCty = new Country(name, code.toUpperCase(), new ArrayList<>());
        LoadGame.MNG.getPlt().getCtys().add(newCty);

        return true;
    }

    /**
     * 打印当前所有国家信息
     *
     * @return 执行状态
     */
    public boolean listInfoCty() {
        StringBuffer info = new StringBuffer();

        listCty(info);
        System.out.println("\n" + info);

        return true;
    }

}
