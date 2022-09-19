package com.gomicroim.lib.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringUtils {
    /**
     * 空字符串判断
     *
     * @param text 字符串
     * @return 结果
     */
    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    /**
     * json转换成uri的参数，只支持一层结构
     *
     * @param json json字符串
     * @return path字符串
     */
    public static Map<String, String> parseJsonToUrlParams(java.lang.String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Iterator<String> it = jsonObject.keys();
        Map<String, String> result = new HashMap<>();
        while (it.hasNext()) {
            java.lang.String key = it.next();
            java.lang.String value = jsonObject.getString(key);
            result.put(key, value);
        }
        return result;
    }
}
