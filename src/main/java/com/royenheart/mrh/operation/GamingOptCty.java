package com.royenheart.mrh.operation;

import com.royenheart.mrh.existence.Country;
import com.royenheart.mrh.sysio.SysOutErr;
import com.royenheart.mrh.sysio.SysOutMain;
import com.royenheart.mrh.sysio.SysOutTip;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class GamingOptCty extends GamingOpt {

    private final GamingOptListInfo listInfo;
    private final SysOutMain out;
    private final SysOutTip tip;
    private final SysOutErr err;

    public GamingOptCty() {
        listInfo = new GamingOptListInfo();
        out = new SysOutMain();
        tip = new SysOutTip();
        err = new SysOutErr();
    }

    /**
     * 添加国家
     * @return 是否添加成功
     */
    public boolean addCty() {
        String name, code;

        out.print("\n现在开始新增国家操作");

        try {
            Method useWhat = CheckParam.class.getMethod("checkCtyName", String.class);
            name = cp.checkParamMethod("请填写国家名字",
                    tip.rangeLimitGen(Country.MIN_NAME_LENGTH, Country.MAX_NAME_LENGTH),
                    useWhat, cp);
            useWhat = CheckParam.class.getMethod("checkCtyCode", String.class);
            code = cp.checkParamMethod("请填写国家编号", "(两位英文字母)", useWhat, cp);
        } catch (NoSuchMethodException e) {
            err.print("检测方法出错，请检查是否正确引用", e);
            return false;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            err.print("参数检测方法拒绝访问或输入非法，请检查方法权限和输入检测是否全面", e);
            return false;
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

        listInfo.listCty(info);
        out.print("\n" + info);

        return true;
    }

}
