package com.royenheart.mrh.operation;

import com.royenheart.mrh.existence.Universe;

import java.util.BitSet;
import java.util.Hashtable;

/**
 * 宇宙操作
 * @author RoyenHeart
 */
public class GamingOpt {

    // 宇宙实例、参数检查实例

    protected final Universe mng;
    protected final CheckParam cp;

    /**
     * 卫星查找规则集开启情况，公有
     */
    protected static final BitSet RULES_ON = new BitSet();
    /**
     * 卫星查找规则集索引，公有
     * <p>
     *     索引单词 - 对应规则集位置
     * </p>
     */
    protected static final Hashtable<String, Integer> RULES = new Hashtable<>();

    public GamingOpt() {
        // 获取唯一的宇宙和检查参数
        mng = Universe.getMng();
        cp = new CheckParam();
    }

    /**
     * 初始化(重置)规则集
     */
    public static void initRules() {
        // 规则-BitMap索引键值对初始化
        RULES.put("name", 0);
        RULES.put("cosparid", 1);
        RULES.put("caseLockOn", 2);
        RULES.put("fuzzySearchOn", 3);

        // 规则初始开启状态初始化
        RULES_ON.set(RULES.get("name"), true);
        RULES_ON.set(RULES.get("cosparid"), false);
        RULES_ON.set(RULES.get("caseLockOn"));
        RULES_ON.set(RULES.get("fuzzySearchOn"));
    }

}
