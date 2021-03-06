package com.royenheart.mrh.gamedata;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.royenheart.mrh.existence.Planet;
import com.royenheart.mrh.existence.Universe;
import com.royenheart.mrh.sysio.SysOutErr;
import com.royenheart.mrh.sysio.SysOutTip;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 游戏数据载入操作
 * <p>
 *     载入游戏数据（行星数据）
 *     当结束游戏时由DataStore完成游戏数据的存储
 * </p>
 * @author RoyenHeart
 */
public class DataRead {

    private final SysOutErr err;
    private final SysOutTip tip;

    // 单例设计模式，一次游戏只允许一个DataRead

    private static final DataRead LD = new DataRead();
    private DataRead() {
        tip = new SysOutTip();
        err = new SysOutErr();
    }
    public static DataRead getLd() {
        return LD;
    }

    /**
     * 初始化已有的mng宇宙数据，包括所有的行星，国家，卫星
     * <p>
     *     同时进行行星选择，删除
     * </p>
     * @throws IOException 无法从文件读入，可能是设定的存储数据的目录不存在
     */
    public void initial() throws IOException {
        Gson gson = new Gson();

        String path = "resources/planets";
        File file = new File(path);

        try {
            if (mkdirResources(file)) {
                tip.print(path+"目录不存在，已创建");
            }
        } catch (SecurityException e) {
            err.print(path+"目录创建失败，请检查父目录是否存在或者目录权限是否合适", e);
            System.exit(-1);
        }

        File[] fs = file.listFiles();

        // 遍历目录下的json文件
        for (File f : fs) {
            if (f.isFile() && f.getName().matches(".*.json")) {
                Path fp = f.toPath();
                Reader reader = Files.newBufferedReader(fp, StandardCharsets.UTF_8);
                if (f.length() <= 0) {
                    err.print(f.getName()+":文件内容为空，已跳过", new Exception("json文件为空"));
                    break;
                }
                try {
                    Planet plt = gson.fromJson(reader, Planet.class);
                    Universe.getMng().addPlt(plt);
                } catch (JsonSyntaxException e) {
                    err.print(f.getName() + ":json格式错误，已跳过", e);
                }
                reader.close();
            }
        }
    }

    private boolean mkdirResources(File make) throws SecurityException {
        return make.mkdirs();
    }

}
