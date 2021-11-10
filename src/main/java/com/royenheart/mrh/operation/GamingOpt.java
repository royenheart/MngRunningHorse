package com.royenheart.mrh.operation;

import com.royenheart.SysIn;
import com.royenheart.mrh.existence.Country;
import com.royenheart.mrh.existence.Planet;
import com.royenheart.mrh.existence.Satellite;
import com.royenheart.mrh.existence.Universe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;

/**
 * 宇宙操作
 * @author RoyenHeart
 */
public class GamingOpt {

    // 宇宙实例、参数检查实例

    private final Universe mng;
    private final CheckParam cp;

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

        // 获取宇宙实例，参数检查实例

        mng = Universe.getMng();
        cp = CheckParam.getCp();
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
     * @return 是否配置成功
     */
    public boolean setRules() {
        String command = "true";

        System.out.println("\n现在开始配置卫星查找的规则集");
        System.out.println("请输入表示真假的单词设置规则开启状态");
        listRules();

        for (String s : rules.keySet()) {

            try {
                Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
                command = cp.checkParamMethod("请填写", s, useWhat);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }

            setRulesOn(s, cp.getTrueFalseFromIn(command));
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
     * @param param 待查找的规则名
     * @return 返回该规则开启情况
     */
    public boolean getRulesOn(String param) {
        return rulesOn.get(rules.get(param));
    }

    /**
     * 添加卫星
     * @return 是否添加成功
     */
    public boolean addSat() {
        String name = "";
        double dis = 0;
        /* 待添加的卫星轨道价值 */
        double disValue = 0;
        String cosparid = null;
        Country belongCty = null;
        boolean used = false;
        StringBuffer info = new StringBuffer();
        Hashtable<Method, String> params = new Hashtable<>();
        Hashtable<String, String> limits = new Hashtable<>();

        System.out.println("\n当前已有国家和卫星");
        listCty(info);
        listSat(info);
        System.out.println(info);

        if (mng.getPlt().getAmountsCty() <= 0) {
            System.out.println("当前不存在任何国家，无法执行添加卫星操作，请建立起一个国家后再来");
            return false;
        }

        try {
            params.put(CheckParam.class.getMethod("checkSatName", String.class), "卫星名字");
            params.put(CheckParam.class.getMethod("checkSatDis", String.class), "卫星轨道半径");
            params.put(CheckParam.class.getMethod("checkSatTruckValue", String.class), "卫星轨道价值");
            params.put(CheckParam.class.getMethod("checkSatCos", String.class), "卫星cosparid");
            params.put(CheckParam.class.getMethod("checkSatUsed", String.class), "卫星使用情况");
            limits.put("卫星名字", String.format("(长度范围:[%d-%d])", Satellite.MIN_NAME_LENGTH, Satellite.MAX_NAME_LENGTH));
            limits.put("卫星轨道半径", String.format("(大小范围:[%.2f-%.2f],且与其他卫星距离不小于0.2)", Satellite.MIN_DIS, Satellite.MAX_DIS));
            limits.put("卫星轨道价值", String.format("(大小范围:[%.2f-%.2f])", Satellite.MIN_VALUE, Satellite.MAX_VALUE));
            limits.put("卫星cosparid", "(格式为两位英文字母加4位数字序号)");
            limits.put("卫星使用情况", "(使用表示真假的单词)");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println("现在开始添加卫星，请添加对应的信息");
        System.out.println("卫星使用状态请填写表示真假的单词");

        /*
        遍历param获取方法
         */
        for (Method method : params.keySet()) {
            String keyVal = null;

            /* 获取合法参数 */
            try {
                String param = params.get(method);
                keyVal = cp.checkParamMethod("请填写", param + limits.get(param), method);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }

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
                    used = cp.getTrueFalseFromIn(keyVal);
                    break;
                default:
                    System.out.println("未知键值对，操作中断");
                    return false;
            }
        }

        // 添加卫星至行星

        Satellite newSat = new Satellite(name, cosparid, dis, disValue, used);

        // 根据cosparid获取所属国家
        for (Country cty : mng.getPlt().getCtys()) {
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
     * 根据关键字查找所有符合条件的卫星，组成列表
     * <p>
     *     可以自行开关下列的规则
     *     1. 可开启模糊查找
     *     2. 可开启忽略大小写
     *     3. 可根据名称查找卫星
     *     4. 可根据cosparid查找卫星
     *     其中名称查找和cosparid不能同时开启
     *     之后将列表返回给调用函数，调用函数再交由listSat(info, sats)得出输出结果
     * </p>
     * @param param 查找的参数
     * @return 查找到的卫星对象列表
     */
    private ArrayList<Satellite> findSatList(String param) {
        /*
          查找到的卫星列表
         */
        ArrayList<Satellite> sats = new ArrayList<>();

        if (mng.getPlt().getAmountsSat() <= 0) {
            // 不存在卫星的情况
            return null;
        } else if (param.isEmpty()) {
            // 输入参数为空的情况（未查找到卫星）
            return sats;
        }
        for (Country cty : mng.getPlt().getCtys()) {
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

        return sats;
    }

    /**
     * 调用findSatList，并选取指定的卫星
     * <p>
     *     选取卫星，返回给调用函数，且针对不同情况输出错误
     * </p>
     * @return 选取的单个卫星
     */
    private Satellite findSat() {
        String findParam;
        String command = "";
        ArrayList<Satellite> list = null;
        Satellite sat;
        StringBuffer info = new StringBuffer();

        if (mng.getPlt().getAmountsSat() <= 0) {
            System.out.println("当前不存在卫星");
            return null;
        }
        findParam = SysIn.nextLine();
        list = findSatList(findParam);
        if (list.size() == 0) {
            System.out.println("未查找到卫星");
            return null;
        }

        listSat(info, list);
        System.out.println(info);

        if (list.size() > 1) {
            System.out.println("请输入卫星前的序号选择卫星");
            command = SysIn.nextLine();
            command = cp.checkCommandInRange(command, 1, list.size());
            sat = list.get(Integer.parseInt(command) - 1);
        } else {
            try {
                Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
                command = cp.checkParamMethod("确认选择该卫星", list.get(0).toString()+"?\n(使用表示真假的单词)", useWhat);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (cp.getTrueFalseFromIn(command)) {
                sat = list.get(0);
            } else {
                System.out.println("放弃操作");
                return null;
            }
        }

        System.out.println("已选择");
        System.out.println(sat);
        return sat;
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

    /**
     * 打印出当前行星信息和全部已有的卫星列表
     * @return 行星信息和卫星列表
     */
    public boolean listInfoAll() {
        StringBuffer info = new StringBuffer();

        info.append("\n现在开始列出当前活动行星信息（包括行星信息、国家信息、卫星信息）\n");

        listPlt(info);
        listCty(info);
        // 卫星的打印直接交给重载函数listSat(String info)进行输出
        listSat(info);

        System.out.println(info);

        return true;
    }

    /**
     * 查找停运的卫星列表并打印出来
     * <p>
     *      先得出列表，再交给listSat进行打印
     * </p>
     * @return 是否查找成功
     */
    public boolean findNoUseSat() {
        StringBuffer info = new StringBuffer();
        ArrayList<Satellite> list = new ArrayList<>();

        for (Country cty : mng.getPlt().getCtys()) {
            for (Satellite e : cty.getSats()) {
                if (!e.getUsed()) {
                    list.add(e);
                }
            }
        }

        System.out.println("\n现在显示停用卫星列表");
        if (list.size() == 0 && mng.getPlt().getAmountsSat() > 0) {
            System.out.println("\n全部卫星已启用");
        } else {
            listSat(info, list);
            System.out.println(info);
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
     * @return 是否正确查找
     */
    public boolean findSatListInfo() {
        StringBuffer info = new StringBuffer();

        System.out.println("\n现在开始查找卫星，请输入查找的关键字");

        listRules();

        if (mng.getPlt().getAmountsSat() <= 0) {
            System.out.println("当前不存在卫星");
            return false;
        }

        listSat(info, findSatList(SysIn.nextLine()));
        System.out.println(info);

        return true;
    }

    /**
     * 编辑修改卫星信息
     * @return 是否修改成功
     */
    public boolean editSat() {
        Satellite sat;
        String command;
        String message = "";
        String limit = "";
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
        command = cp.checkCommandInRange(command, 1, 2);

        try {
            switch (Integer.parseInt(command)) {
                case 1:
                    check = CheckParam.class.getMethod("checkSatName", String.class);
                    set = Satellite.class.getMethod("setName", String.class);
                    message = "需要修改为什么名字？";
                    limit = String.format("(长度范围:[%d-%d])", Satellite.MIN_NAME_LENGTH, Satellite.MAX_NAME_LENGTH);
                    break;
                case 2:
                    check = CheckParam.class.getMethod("checkSatTruckValue", String.class);
                    set = Satellite.class.getMethod("setDisValue", String.class);
                    message = "轨道价值修改为多少？";
                    limit = String.format("(大小范围:[%.2f-%.2f])", Satellite.MIN_VALUE, Satellite.MAX_VALUE);
                    break;
                default:
                    System.out.println("序号范围超出，操作中断");
                    return false;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            command = cp.checkParamMethod(message, limit, check);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

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
     * @return 是否删除成功
     */
    public boolean delSat() {
        Satellite sat;
        String command = "";
        System.out.println("\n现在开始删除卫星，请先查找卫星");
        System.out.println("开始查找卫星，请输入查找的关键字");

        listRules();
        sat = findSat();

        if (sat == null) {
            System.out.println("删除卫星操作中断");
            return false;
        }

        try {
            Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
            command = cp.checkParamMethod("是否删除卫星", "(请填入代表真假的单词)", useWhat);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (cp.getTrueFalseFromIn(command)) {
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
     * @return 是否启用成功
     */
    public boolean disEnableSat() {
        Satellite sat;
        String command = "";
        System.out.println("\n现在开始启用、封锁卫星，请先查找卫星");
        System.out.print("开始查找卫星，请输入查找的关键字: ");

        listRules();
        sat = findSat();

        if (sat == null) {
            System.out.println("封锁、启用卫星操作中断");
            return false;
        }

        try {
            Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
            command = cp.checkParamMethod(String.format(
                    "是否%s卫星？:",sat.getUsed()?"封锁":"启用"
            ), "(请填入代表真假的单词)", useWhat);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (!cp.getTrueFalseFromIn(command)) {
            System.out.println("未做任何改变");
        } else {
            System.out.println("卫星已设置为" + (sat.getUsed()?"封锁":"启用"));
            sat.setUsed();
        }
        System.out.println("选中行星当前状态:\n" + sat);

        return true;
    }

    /**
     * 添加国家
     * @return 是否添加成功
     */
    public boolean addCty() {
        String name = "", code = "";

        System.out.println("\n现在开始新增国家操作");

        try {
            Method useWhat = CheckParam.class.getMethod("checkCtyName", String.class);
            name = cp.checkParamMethod("请填写国家名字", String.format(
                    "(长度范围:[%d-%d])",
                    Country.MIN_NAME_LENGTH, Country.MAX_NAME_LENGTH
            ), useWhat);
            useWhat = CheckParam.class.getMethod("checkCtyCode", String.class);
            code = cp.checkParamMethod("请填写国家编号", "(两位英文字母)", useWhat);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        Country newCty = new Country(name, code.toUpperCase(), new ArrayList<>());
        mng.getPlt().getCtys().add(newCty);

        return true;
    }

    /**
     * 打印当前所有国家信息
     * @return 执行状态
     */
    public boolean listInfoCty() {
        StringBuffer info = new StringBuffer();

        listCty(info);
        System.out.println("\n" + info);

        return true;
    }

}
