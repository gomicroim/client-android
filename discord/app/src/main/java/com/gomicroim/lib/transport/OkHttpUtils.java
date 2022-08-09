package com.gomicroim.lib.transport;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

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

    // handler主要用于异步请求数据之后更新UI
    private static final Handler handler = new Handler();

    /**
     * 设置请求的token
     *
     * @param token token字符串
     */
    public static void setToken(String token) {
        TOKEN = token;
    }

    /**
     * GET异步请求
     *
     * @param url      地址
     * @param param    参数
     * @param callback 结果
     */
    public static void getAsync(String url, Map<String, String> param, HttpResponseCallBack callback) {
        Log.i(TAG, "请求地址===》" + url);

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
     * 表单提交数据
     *
     * @param url      请求地址
     * @param formData 表单回调
     * @param callback 响应回调
     */
    public static void postAsyncFormData(String url, Map<String, String> formData, HttpResponseCallBack callback) {
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

        Log.i(TAG, "开始发送请求：请求地址【" + url + "】,请求参数==>" + showData);
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
     * POST 请求，json格式
     *
     * @param url      请求地址
     * @param json     json数据
     * @param callback 响应回调
     */
    public static void postAsyncJson(String url, String json, HttpResponseCallBack callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.
                Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .post(requestBody)
                .build();

        Log.i(TAG, "开始发送请求：请求地址【" + url + "】,请求参数==>" + json);
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
     * PUT 请求，json格式
     *
     * @param url      请求地址
     * @param json     json数据
     * @param callback 响应回调
     */
    public static void putAsyncJson(String url, String json, HttpResponseCallBack callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.
                Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .put(requestBody)
                .build();

        Log.i(TAG, "开始发送请求：请求地址【" + url + "】,请求参数==>" + json);
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
     * PUT 请求, form-data表单
     *
     * @param url      请求地址
     * @param formData 表单数据
     * @param callback 响应回调
     */
    public static void putAsyncForm(String url, Map<String, String> formData, HttpResponseCallBack callback) {
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
        Log.i(TAG, "开始发送请求：请求地址【" + url + "】,请求参数==>" + showData);
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

    private static void onFailure(@NonNull HttpResponseCallBack res,
                                  @NonNull Call call,
                                  @NonNull IOException e) {
        Log.e(TAG, "响应失败===》" + e.getMessage());
        handler.post(() -> {
            res.error(e.getMessage());
        });
    }

    private static void onResponse(@NonNull HttpResponseCallBack res,
                                   @NonNull Call call,
                                   @NonNull Response response) throws IOException {
        String respBody = response.body().string();
        Log.i(TAG, "响应成功===》" + respBody);
        handler.post(() -> {
            try {
                res.success(respBody);
            } catch (JSONException e) {
                e.printStackTrace();
                //ActivityUtils.showLogToast("程序出现异常:" + e.getMessage());
            }
        });
    }
}
