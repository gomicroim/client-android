package com.gomicroim.lib.helper;

import com.gomicroim.lib.service.LoginServiceImpl;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OK Http 工具类 再封装
 */
public class OkHttpUtils {
    private static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().
            readTimeout(15, TimeUnit.SECONDS).
            writeTimeout(15, TimeUnit.SECONDS).
            connectTimeout(15, TimeUnit.SECONDS).build();

    private static final String TAG = "OkHttpUtils";
    private static String TOKEN = "";
    private static String BASE_URL = "";
    private static Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);

    /**
     * 设置请求的token
     *
     * @param token token字符串
     */
    public static void setToken(String token) {
        TOKEN = token;
    }

    /**
     * 设置基础url，后续的请求都在此基础上追加
     *
     * @param baseUrl 基础url
     */
    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    /**
     * 异步GET请求
     *
     * @param url      地址
     * @param param    参数
     * @param callback 结果
     */
    public static void getAsync(String url, Map<String, String> param, HttpResponseCallBack callback) {
        url = getUrl(url);
        LOG.info("请求地址===》{}", url);

        if (param != null) {
            StringBuilder urlBuilder = new StringBuilder();
            for (String key : param.keySet()) {
                urlBuilder.append(key).
                        append("=").
                        append(Objects.requireNonNull(param.get(key))).
                        append("&");
            }
            url = urlBuilder.substring(0, urlBuilder.length() - 1);
        }

        Request request = new Request
                .Builder()
                .addHeader("Authorization", "Bearer " + TOKEN)
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                OkHttpUtils.onFailure(callback, call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                OkHttpUtils.onResponse(callback, call, response);
            }
        });
    }

    /**
     * 异步 POST 请求，x-www-form-urlencoded 表单提交数据
     *
     * @param url      请求地址
     * @param formData 表单回调
     * @param callback 响应回调
     */
    public static void postAsyncFormData(String url, Map<String, String> formData, HttpResponseCallBack callback) {
        url = getUrl(url);
        OkHttpClient client = new OkHttpClient().newBuilder().
                callTimeout(30, TimeUnit.SECONDS)
                .build();
        FormBody.Builder builder = new FormBody.Builder();
        StringBuilder showData = new StringBuilder();
        for (String key : formData.keySet()) {
            builder.add(key, Objects.requireNonNull(formData.get(key)));
            showData.append("   ").append(key).append(":").append(formData.get(key));
        }
        FormBody formBody = builder
                .build();
        Request request = new Request
                .Builder()
                .addHeader("Authorization", "Bearer " + TOKEN)
                .url(url)
                .post(formBody)
                .build();

        LOG.info("开始发送请求：请求地址: {},请求参数: {}", url, showData);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                OkHttpUtils.onFailure(callback, call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                OkHttpUtils.onResponse(callback, call, response);
            }
        });
    }

    /**
     * 异步 POST 请求，application/json 格式
     *
     * @param url      请求地址
     * @param json     json数据
     * @param callback 响应回调
     */
    public static void postAsyncJson(String url, String json, HttpResponseCallBack callback) {
        url = getUrl(url);
        if (json == null) {
            json = "{}";
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.
                Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .post(requestBody)
                .build();

        LOG.info("开始发送请求：请求地址: {},请求参数: {}", url, json);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                OkHttpUtils.onFailure(callback, call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                OkHttpUtils.onResponse(callback, call, response);
            }
        });
    }

    /**
     * 异步 PUT 请求，application/json 格式
     *
     * @param url      请求地址
     * @param json     json数据
     * @param callback 响应回调
     */
    public static void putAsyncJson(String url, String json, HttpResponseCallBack callback) {
        url = getUrl(url);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.
                Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .put(requestBody)
                .build();

        LOG.info("开始发送请求：请求地址: {},请求参数: {}", url, json);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                OkHttpUtils.onFailure(callback, call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                OkHttpUtils.onResponse(callback, call, response);
            }
        });
    }

    /**
     * 异步 PUT 请求, x-www-form-urlencoded 表单提交数据
     *
     * @param url      请求地址
     * @param formData 表单数据
     * @param callback 响应回调
     */
    public static void putAsyncForm(String url, Map<String, String> formData, HttpResponseCallBack callback) {
        url = getUrl(url);

        OkHttpClient client = new OkHttpClient().newBuilder().
                callTimeout(30, TimeUnit.SECONDS)
                .build();
        FormBody.Builder builder = new FormBody.Builder();
        StringBuffer showData = new StringBuffer();
        for (String key : formData.keySet()) {
            builder.add(key, formData.get(key));
            showData.append("   " + key + ":" + formData.get(key));
        }
        FormBody formBody = builder
                .build();
        Request request = new Request
                .Builder()
                .addHeader("Authorization", "Bearer " + TOKEN)
                .url(url)
                .put(formBody)
                .build();
        LOG.info("开始发送请求：请求地址: {},请求参数: {}", url, showData);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                OkHttpUtils.onFailure(callback, call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                OkHttpUtils.onResponse(callback, call, response);
            }
        });
    }

    private static String getUrl(String uri) {
        // return BASE_URL + uri;
        if (uri.contains("?")) {
            return String.format("%s%s&format=json&skip=true", BASE_URL, uri);
        }
        return String.format("%s%s?format=json&skip=true", BASE_URL, uri);
    }

    private static void onFailure(@NotNull HttpResponseCallBack res,
                                  @NotNull Call call,
                                  @NotNull IOException e) {
        LOG.error("响应失败===》{}", e.getMessage());
        res.onException(e);
    }

    private static void onResponse(@NotNull HttpResponseCallBack res,
                                   @NotNull Call call,
                                   @NotNull Response response) throws IOException {
        String respBody = response.body().string();
        LOG.debug("响应成功===》{}", respBody);
        try {
            res.onSuccess(respBody);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            res.onException(e);
            //ActivityUtils.showLogToast("程序出现异常:" + e.getMessage());
        }
    }
}
