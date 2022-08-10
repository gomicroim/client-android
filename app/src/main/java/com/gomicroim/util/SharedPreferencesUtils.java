package com.gomicroim.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 这是一个SharePreference的根据类，使用它可以更方便的数据进行简单存储
 * 这里只要知道基本调用方法就可以了
 * 1.通过构造方法来传入上下文和文件名
 * 2.通过putValue方法传入一个或多个自定义的ContentValue对象，进行数据存储
 * 3.通过get方法来获取数据
 * 4.通过clear方法来清除这个文件的数据
 * 这里没有提供清除单个key的数据，是因为存入相同的数据会自动覆盖，没有必要去理会
 */
public class SharedPreferencesUtils {
    // 上下文对象
    private static SharedPreferences sharedPreferences = null;
    private static final String kSettingFileName = "setting.xml";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(kSettingFileName, Context.MODE_PRIVATE);
    }
}
