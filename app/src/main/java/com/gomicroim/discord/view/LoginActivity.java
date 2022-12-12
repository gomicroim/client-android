package com.gomicroim.discord.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gomicroim.discord.MainActivity;
import com.gomicroim.discord.R;
import com.gomicroim.discord.helper.SharedPreferencesHelper;
import com.gomicroim.discord.widget.LoadingDialog;
import com.gomicroim.lib.Api;
import com.gomicroim.lib.ApiOptions;
import com.gomicroim.lib.LoginInfo;
import com.gomicroim.lib.model.dto.LoginReply;
import com.gomicroim.lib.protos.user.User;
import com.gomicroim.lib.service.LoginServiceImpl;
import com.gomicroim.lib.transport.RequestCallback;
import com.gomicroim.lib.util.AndroidDeviceId;
import com.gomicroim.lib.util.ProtoJsonUtils;
import com.gomicroim.lib.util.StringUtils;
import com.gomicroim.lib.util.TokenUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText etName;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox cbRememberPwd;
    private ImageView ivSeePassword;

    private LoadingDialog loadingDialog; //显示正在加载的对话框
    private SharedPreferencesHelper preferencesHelper;
    private Logger log = LoggerFactory.getLogger(LoginActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferencesHelper = new SharedPreferencesHelper(LoginActivity.this);

        btnLogin = findViewById(R.id.btn_login);
        etName = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        cbRememberPwd = findViewById(R.id.checkBox_password);
        ivSeePassword = findViewById(R.id.iv_see_password);

        btnLogin.setOnClickListener(this);
        cbRememberPwd.setOnCheckedChangeListener(this);
        ivSeePassword.setOnClickListener(this);

        Api.init(ApiOptions.DEFAULT, getLoginInfo());

        loadData();
        autoLogin();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            login();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == cbRememberPwd) {
            preferencesHelper.saveRememberPwdChecked(cbRememberPwd.isChecked());
        }
    }

    private void loadData() {
        etName.setText(preferencesHelper.readPhone());
        cbRememberPwd.setChecked(preferencesHelper.readRememberPwdChecked());
    }

    private LoginInfo getLoginInfo() {
        SharedPreferencesHelper.TokenInfo token = preferencesHelper.readTokenInfo();
        if (token == null) {
            return null;
        }
        if (TokenUtils.isTokenExpires(token.atExpires)) {
            return null;
        }
        return new LoginInfo(token.accessToken);
    }

    private void autoLogin() {
        SharedPreferencesHelper.TokenInfo token = preferencesHelper.readTokenInfo();
        if (token == null) {
            return;
        }

        if (TokenUtils.isTokenExpires(token.atExpires)) {
            if (TokenUtils.isTokenExpires(token.rtExpires)) {
                Toast.makeText(LoginActivity.this, "登录已过期，请重新登录！", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginActivity that = this;
            // refresh accessToken
            Api.getLoginService().refreshToken(token.refreshToken).setCallback(new RequestCallback<User.RefreshTokenReply>() {
                @Override
                public void onSuccess(User.RefreshTokenReply param) {
                    log.info("refresh token success");

                    runOnUiThread(() -> {
                        preferencesHelper.saveTokenInfo(new SharedPreferencesHelper.TokenInfo(
                                token.userId,
                                param.getToken().getAccessToken(),
                                param.getToken().getAtExpires(),
                                param.getToken().getRefreshToken(),
                                param.getToken().getRtExpires()));

                        User.TokenInfo t = User.TokenInfo.newBuilder().
                                setAccessToken(param.getToken().getAccessToken()).
                                setAtExpires(param.getToken().getAtExpires()).
                                setRefreshToken(param.getToken().getRefreshToken()).
                                setRtExpires(param.getToken().getRtExpires()).build();
                        Api.getLoginService().autoLogin(t);
                    });
                }

                @Override
                public void onFailed(int code, String message, Throwable exception) {
                    that.onFailed(code, message, exception);
                }
            });
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void login() {
        showLoading();

        preferencesHelper.savePhone(etName.getText().toString().trim());
        LoginActivity that = this;
        String deviceId = new AndroidDeviceId(this).getUniqueDeviceId();
        User.RegisterRequest req = User.RegisterRequest.newBuilder().setDeviceId(deviceId).setOsVersion("android").build();

        // step: 1 - register
        Api.getLoginService().deviceRegister(req).setCallback(new RequestCallback<User.RegisterReply>() {
            @Override
            public void onSuccess(User.RegisterReply param) {

                // step: 2 - login
                Api.getLoginService().login("86" + etName.getText().toString().trim(),
                        etPassword.getText().toString().trim(),
                        "1.0").setCallback(new RequestCallback<User.AuthReply>() {
                    @Override
                    public void onSuccess(User.AuthReply param) {
                        runOnUiThread(() -> {

                            // 保存token，下次自动登录
                            preferencesHelper.saveTokenInfo(new SharedPreferencesHelper.TokenInfo(
                                    param.getUserId(),
                                    param.getToken().getAccessToken(),
                                    param.getToken().getAtExpires(),
                                    param.getToken().getRefreshToken(),
                                    param.getToken().getRtExpires()));

                            hideLoading();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        });
                    }

                    @Override
                    public void onFailed(int code, String message, Throwable exception) {
                        that.onFailed(code, message, exception);
                    }
                });
            }

            @Override
            public void onFailed(int code, String message, Throwable exception) {
                that.onFailed(code, message, exception);
            }
        });
    }

    // 显示加载的进度款
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
        }
        loadingDialog.show();
    }

    // 隐藏加载的进度框
    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.hide();
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public void onFailed(int code, String message, Throwable exception) {
        runOnUiThread(() -> {
            if (exception != null) {
                Toast.makeText(LoginActivity.this, "登录异常", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "登录失败:" + message, Toast.LENGTH_SHORT).show();
            }
            hideLoading();
        });
    }
}