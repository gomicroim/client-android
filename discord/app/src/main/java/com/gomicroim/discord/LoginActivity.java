package com.gomicroim.discord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.gomicroim.widget.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText etName;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox cbRememberPwd;
    private CheckBox cbAutoLogin;
    private ImageView ivSeePassword;

    private LoadingDialog mLoadingDialog; //显示正在加载的对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        etName = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        cbRememberPwd = findViewById(R.id.checkBox_password);
        cbAutoLogin = findViewById(R.id.checkBox_login);
        ivSeePassword = findViewById(R.id.iv_see_password);

        btnLogin.setOnClickListener(this);
        cbRememberPwd.setOnCheckedChangeListener(this);
        cbAutoLogin.setOnCheckedChangeListener(this);
        ivSeePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}