package com.royenheart.mrh.operation;

import com.royenheart.mrh.existence.Country;
import com.royenheart.mrh.existence.Satellite;
import com.royenheart.mrh.sysio.SysIn;
import com.royenheart.mrh.sysio.SysOutErr;
import com.royenheart.mrh.sysio.SysOutMain;
import com.royenheart.mrh.sysio.SysOutTip;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 卫星操作
 * @author RoyenHeart
 */
public class GamingOptSat extends GamingOpt {

    private final GamingOptListInfo listInfo;
    private final GamingOptRules optRules;
    private final SysOutMain out;
    private final SysOutTip tip;
    private final SysOutErr err;

    public GamingOptSat() {
        optRules = new GamingOptRules();
        listInfo = new GamingOptListInfo();
        out = new SysOutMain();
        tip = new SysOutTip();
        err = new SysOutErr();
    }

    /**
     * 添加卫星
     * @return 是否添加成功
     */
    public boolean addSat() {
        String name = "";
        double dis = 0;
        // 待添加的卫星轨道价值
        double disValue = 0;
        String cosparid = null;
        // 待添加的卫星的所属国家
        Country belongCty = null;
        boolean used = false;
        StringBuffer info = new StringBuffer();
        Hashtable<Method, String> params = new Hashtable<>();
        Hashtable<String, String> limits = new Hashtable<>();

        out.print("\n当前已有国家和卫星");
        listInfo.listCty(info);
        listInfo.listSat(info);
        out.print(info);

        if (mng.getPlt().getAmountsCty() <= 0) {
            tip.print("当前不存在任何国家，无法执行添加卫星操作，请建立起一个国家后再来");
            return false;
        }

        try {
            params.put(CheckParam.class.getMethod("checkSatName", String.class), "卫星名字");
            params.put(CheckParam.class.getMethod("checkSatDis", String.class), "卫星轨道半径");
            params.put(CheckParam.class.getMethod("checkSatTruckValue", String.class), "卫星轨道价值");
            params.put(CheckParam.class.getMethod("checkSatCos", String.class), "卫星cosparid");
            params.put(CheckParam.class.getMethod("checkSatUsed", String.class), "卫星使用情况");
            limits.put("卫星名字", tip.rangeLimitGen(Satellite.MIN_NAME_LENGTH, Satellite.MAX_NAME_LENGTH));
            limits.put("卫星轨道半径", tip.rangeLimitGen(Satellite.MIN_DIS, Satellite.MAX_DIS)+"(且与其他卫星距离不小于0.2)");
            limits.put("卫星轨道价值", tip.rangeLimitGen(Satellite.MIN_VALUE, Satellite.MAX_VALUE));
            limits.put("卫星cosparid", "(格式为两位英文字母加4位数字序号)");
            limits.put("卫星使用情况", "(使用表示真假的单词)");
        } catch (NoSuchMethodException e) {
            err.print("未找到相关方法，请检查卫星检测方法操作列表是否添加正确?", e);
            return false;
        }

        out.print("现在开始添加卫星，请添加对应的信息");
        out.print("卫星使用状态请填写表示真假的单词");

        // 遍历param获取不同参数对应的检测方法
        for (Method method : params.keySet()) {
            String keyVal;

            // 获取合法参数
            try {
                String param = params.get(method);
                keyVal = cp.checkParamMethod("请填写", param + limits.get(param), method, cp);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
                return false;
            }

            // 根据不同value值选择对应的参数变量进行赋值
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
                    tip.print("未知键值对，操作中断");
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
        out.print("卫星已添加\n" + newSat);

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
        // 查找到的卫星列表
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
                if (optRules.getRulesOn("caseLockOn")) {
                    satName = satName.toLowerCase();
                    param = param.toLowerCase();
                }
                if (optRules.getRulesOn("name")) {
                    if (optRules.getRulesOn("fuzzySearchOn")) {
                        if (satName.contains(param)) {
                            sats.add(sat);
                        }
                    } else {
                        if (satName.equals(param)) {
                            sats.add(sat);
                        }
                    }
                }
                if (optRules.getRulesOn("cosparid")) {
                    if (optRules.getRulesOn("fuzzySearchOn")) {
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
        String command;
        ArrayList<Satellite> list;
        Satellite sat;
        StringBuffer info = new StringBuffer();

        if (mng.getPlt().getAmountsSat() <= 0) {
            tip.print("当前不存在卫星");
            return null;
        }
        findParam = SysIn.nextLine();
        list = findSatList(findParam);
        if (list.size() == 0) {
            tip.print("未查找到卫星");
            return null;
        }

        listInfo.listSat(info, list);
        out.print(info);

        if (list.size() > 1) {
            out.print("请输入卫星前的序号选择卫星");
            command = cp.checkCommandInRange(SysIn.nextLine(), 1, list.size());
            sat = list.get(Integer.parseInt(command) - 1);
        } else {
            try {
                Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
                command = cp.checkParamMethod("确认选择该卫星\n", list.get(0).toString()+"(使用表示真假的单词)?", useWhat, cp);
            } catch (NoSuchMethodException e) {
                err.print("检测方法出错，请检查是否正确引用", e);
                return null;
            } catch (IllegalAccessException | IllegalArgumentException e) {
                err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
                return null;
            }
            if (cp.getTrueFalseFromIn(command)) {
                sat = list.get(0);
            } else {
                out.print("放弃操作");
                return null;
            }
        }

        out.print("已选择");
        out.print(sat.toString());
        return sat;
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

        // 获取处于封锁的卫星信息
        for (Country cty : mng.getPlt().getCtys()) {
            for (Satellite e : cty.getSats()) {
                if (!e.getUsed()) {
                    list.add(e);
                }
            }
        }

        out.print("\n现在显示停用卫星列表");
        if (list.size() == 0 && mng.getPlt().getAmountsSat() > 0) {
            out.print("\n全部卫星已启用");
        } else {
            listInfo.listSat(info, list);
            out.print(info);
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

        out.print("\n现在开始查找卫星，请输入查找的关键字");

        // 列出当前查找规则集
        out.print(optRules.listRules(new StringBuffer()));

        if (mng.getPlt().getAmountsSat() <= 0) {
            tip.print("当前不存在卫星");
            return false;
        }

        listInfo.listSat(info, findSatList(SysIn.nextLine()));
        out.print(info);

        return true;
    }

    /**
     * 编辑修改卫星信息
     * @return 是否修改成功
     */
    public boolean editSat() {
        Satellite sat;
        String command;
        String message;
        String limit;
        // 不同信息修改时对应的参数检测的方法
        Method check;
        Method set;
        out.print("\n现在开始修改卫星信息");
        out.print("可修改名字、轨道价值，请先根据关键词查找卫星");

        // 列出当前查找规则集
        out.print(optRules.listRules(new StringBuffer()));

        sat = findSat();

        if (sat == null) {
            out.print("编辑修改卫星操作中断");
            return false;
        }

        out.print(String.format(
                "需要修改什么参数(选择参数前的序号)？\n1.%s\n2.%s\n",
                "卫星名字","轨道价值"
        ));

        command = cp.checkCommandInRange(SysIn.nextLine(), 1, 2);

        try {
            // 根据填入的参数选择不同的卫星信息进行修改
            switch (Integer.parseInt(command)) {
                case 1:
                    check = CheckParam.class.getMethod("checkSatName", String.class);
                    set = Satellite.class.getMethod("setName", String.class);
                    message = "需要修改为什么名字？";
                    limit = tip.rangeLimitGen(Satellite.MIN_NAME_LENGTH, Satellite.MAX_NAME_LENGTH);
                    break;
                case 2:
                    check = CheckParam.class.getMethod("checkSatTruckValue", String.class);
                    set = Satellite.class.getMethod("setDisValue", String.class);
                    message = "轨道价值修改为多少？";
                    limit = tip.rangeLimitGen(Satellite.MIN_VALUE, Satellite.MAX_VALUE);
                    break;
                default:
                    tip.print("序号范围超出，操作中断");
                    return false;
            }
        } catch (NoSuchMethodException e) {
            err.print("检测方法出错，请检查是否正确引用", e);
            return false;
        }

        try {
            command = cp.checkParamMethod(message, limit, check, cp);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
            return false;
        }

        out.print("卫星\n" + sat + "已被更改为");
        try {
            set.invoke(sat,  command);
        } catch (IllegalAccessException | InvocationTargetException e) {
            err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
            return false;
        }
        out.print(sat.toString());

        return true;
    }

    /**
     * 删除卫星
     * @return 是否删除成功
     */
    public boolean delSat() {
        Satellite sat;
        String command;
        out.print("\n现在开始删除卫星，请先查找卫星");
        out.print("开始查找卫星，请输入查找的关键字");

        // 列出当前查找规则集
        out.print(optRules.listRules(new StringBuffer()));
        sat = findSat();

        if (sat == null) {
            tip.print("删除卫星操作中断");
            return false;
        }

        try {
            Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
            command = cp.checkParamMethod("是否删除卫星", "(请填入代表真假的单词)", useWhat, cp);
        } catch (NoSuchMethodException e) {
            err.print("检测方法出错，请检查是否正确引用", e);
            return false;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
            return false;
        }

        if (cp.getTrueFalseFromIn(command)) {
            sat.inWhichCountry().getSats().removeIf(e -> e.equals(sat));
            out.print("卫星" + sat.getCosparid() + ":" + sat.getName() + "已被删除");
            return true;
        } else {
            out.print("删除卫星操作中断");
            return false;
        }
    }

    /**
     * 查找卫星并根据传入参数启用、封锁卫星
     * @return 是否启用成功
     */
    public boolean disEnableSat() {
        Satellite sat;
        String command;
        out.print("\n现在开始启用、封锁卫星，请先查找卫星");
        out.print("开始查找卫星，请输入查找的关键字: ");

        // 列出当前查找规则集
        out.print(optRules.listRules(new StringBuffer()));
        sat = findSat();

        if (sat == null) {
            out.print("封锁、启用卫星操作中断");
            return false;
        }

        try {
            Method useWhat = CheckParam.class.getMethod("checkTrueFalse", String.class);
            command = cp.checkParamMethod(String.format(
                    "是否%s卫星？:",sat.getUsed()?"封锁":"启用"
            ), "(请填入代表真假的单词)", useWhat, cp);
        } catch (NoSuchMethodException e) {
            err.print("检测方法出错，请检查是否正确引用", e);
            return false;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
            return false;
        }

        if (!cp.getTrueFalseFromIn(command)) {
            out.print("未做任何改变");
        } else {
            out.print("卫星已设置为" + (sat.getUsed()?"封锁":"启用"));
            sat.setUsed();
        }
        out.print("选中行星当前状态:\n" + sat);

        return true;
    }

}
