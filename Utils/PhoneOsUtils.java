package com.mimikko.mimikkoui.toolkit_library.system;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ 创建者 qinf
 */
public class PhoneOsUtils {

    private static final String TAG = "PhoneOsUtils";

    private static final PhoneOsUtils sInstance = new PhoneOsUtils();

    private Map<String, String> mMap;

    private PhoneOsUtils() {

    }

    public static final PhoneOsUtils getInstance() {
        return sInstance;
    }

    private boolean isNull(String str) {
        if (str == null || str.trim().length() <= 0) {
            return true;
        }
        return false;

    }

    public String getOsVersion(Context context) {
        String version = getRomVersion(context, "ro.miui.ui.version.name");
        if (!isNull(version) && !version.equals("fail")) {
            return "XiaoMi/MIUI/" + version;
        }
        version = getRomVersion(context, "ro.build.version.emui");
        if (!isNull(version) && !version.equals("fail")) {
            return "HuaWei/EMOTION/" + version;
        }
        // 一加未经过测试
//        version = getRomVersion(context, "ro.rom.version");
//        if (!isNull(version) && !version.equals("fail")) {
//            return "OnePlus/" + version;
//        }
        version = getRomVersion(context, "ro.lenovo.series");
        if (isNull(version) || version.equals("fail")) {
            version = getRomVersion(context, "ro.build.nubia.rom.name");
            if (!isNull(version) && !version.equals("fail")) {
                return "Zte/NUBIA/" + version + "_" + getRomVersion(context, "ro.build.nubia.rom.code");
            }
            version = getRomVersion(context, "ro.meizu.product.model");
            if (!isNull(version) && !version.equals("fail")) {
                return "Meizu/FLYME/" + getRomVersion(context, "ro.build.display.id");
            }
            version = getRomVersion(context, "ro.build.version.opporom");
            if (!isNull(version) && !version.equals("fail")) {
                return "Oppo/COLOROS/" + version;
            }
            version = getRomVersion(context, "ro.vivo.os.build.display.id");
            if (!isNull(version) && !version.equals("fail")) {
                return "vivo/FUNTOUCH/" + version;
            }
            version = getRomVersion(context, "ro.aa.romver");
            if (!isNull(version) && !version.equals("fail")) {
                return "htc/" + version + "/" + getRomVersion(context, "ro.build.description");
            }
            version = getRomVersion(context, "ro.lewa.version");
            if (!isNull(version) && !version.equals("fail")) {
                return "tcl/" + version + "/" + getRomVersion(context, "ro.build.display.id");
            }
            version = getRomVersion(context, "ro.gn.gnromvernumber");
            if (!isNull(version) && !version.equals("fail")) {
                return "amigo/" + version + "/" + getRomVersion(context, "ro.build.display.id");
            }
            version = getRomVersion(context, "ro.build.tyd.kbstyle_version");
            if (isNull(version) || version.equals("fail")) {
                return getRomVersion(context, "ro.build.fingerprint") + "/" + getRomVersion(context, "ro.build.rom.id");
            }
            return "dido/" + version;
        }
        return "Lenovo/VIBE/" + getRomVersion(context, "ro.build.version.incremental");
    }


    private String getRomVersion(Context context, String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        if (mMap == null) {
            mMap = new HashMap();
            List<String> propLists = readSystemConfig(context, "getprop");
            if (propLists != null && propLists.size() > 0) {
                Pattern compile = Pattern.compile("\\[(.+)\\]: \\[(.*)\\]");
                Iterator var4 = propLists.iterator();
                while (var4.hasNext()) {
                    String info = (String) var4.next();
                    Matcher matcher = compile.matcher(info);
                    if (matcher.find()) {
                        mMap.put(matcher.group(1), matcher.group(2));
                    }
                }
            }
        }
        return mMap.containsKey(str) ? mMap.get(str) : "fail";

    }


    public ArrayList<String> readSystemConfig(Context context, String name) {
        BufferedReader inputReader = null;
        BufferedReader errorReader = null;
        ArrayList infoLists = new ArrayList();

        ArrayList list;
        try {
            String dir = "/system/bin/sh";
            if (!(new File(dir)).exists() || !(new File(dir)).canExecute()) {
                dir = "sh";
            }

            list = new ArrayList(Arrays.asList(dir, "-c"));
            list.add(name);
            Process process = Runtime.getRuntime().exec((String[]) list.toArray(new String[3]));
            inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String str;
            while ((str = inputReader.readLine()) != null) {
                infoLists.add(str);
            }

            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((str = errorReader.readLine()) != null) {
                infoLists.add(str);
            }

            return infoLists;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            list = null;
        } finally {
            if (inputReader != null) {
                try {
                    inputReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (errorReader != null) {
                try {
                    errorReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return list;
    }
}
