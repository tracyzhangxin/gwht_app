package com.example.administrator.gwht;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;

import Model.LoginModel;
import Model.UserInfoModel;
import Tools.AppApi;
import Tools.PreferenceUtil;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.email)
    private AutoCompleteTextView mEmailView;
    @ViewInject(R.id.password)
    private EditText mPasswordView;
    @ViewInject(R.id.email_sign_in_button)
    private Button mEmailSignInButton;
    @ViewInject(R.id.login_progress)
    private View mProgressView;
    @ViewInject(R.id.remember_checkbox)
    private CheckBox remember_checkbox;

    public static final String SP_INFOS = "SPDATA_Files";
    public static final String USERID = "UserID";
    public static final String PASSWORD = "PassWord";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        setSpInfos();
       /* Map<String, String> map =PreferenceUtil.getUserInfo(this);
        Toast.makeText(x.app(), map.get("channelid"), Toast.LENGTH_LONG).show();*/
    }

    @Event(value = R.id.password, type = TextView.OnEditorActionListener.class)
    private boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
           attemptLogin();
            return true;
        }
        return false;
    }
    @Event(value = R.id.email_sign_in_button)
    private void onSinginClick(View view) {
        //Toast.makeText(x.app(), "登录成功", Toast.LENGTH_LONG).show();
        attemptLogin();
    }

    /**
     * 用Sp存储用户名和密码
     **/
    @Event(value = R.id.remember_checkbox, type = CompoundButton.OnCheckedChangeListener.class)
    private void onRememberClick(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        // 获得Preferences
        SharedPreferences.Editor editor = sp.edit(); // 获得Editor
        if (isChecked) {
            editor.putString(USERID, this.mEmailView.getText().toString()); // 将用户的帐号存入Preferences
            editor.putString(PASSWORD, this.mPasswordView.getText().toString()); // 将密码存入Preferences
        } else {
            editor.putString(USERID, null); // 不选中，设置为空
            editor.putString(PASSWORD, null);
        }
        editor.apply();
    }

    /**
     * 设置Preferences存储，保存账号和密码
     **/
    private void setSpInfos() {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        String uid = sp.getString(USERID, null); // 取Preferences中的帐号
        String pwd = sp.getString(PASSWORD, null); // 取Preferences中的密码
        if (uid != null && pwd != null) {
            this.mEmailView.setText(uid); // 给EditText控件赋帐号
            this.mPasswordView.setText(pwd); // 给EditText控件赋密码
            remember_checkbox.setChecked(true);
        }
    }
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
          //  showProgress(true);
            userLogin(email, password);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /** 请求后台进行登录 **/
    private void userLogin(final String email, final String password) {

        RequestParams requestParams = new RequestParams(AppApi.LOGIN_URL);
        requestParams.addBodyParameter("userid", email);
        requestParams.addBodyParameter("password", password);

        final Context _this = this;
        //指定回调函数的返回类型为JSON
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                LoginModel data = gson.fromJson(result, LoginModel.class);
                if (data.code == 0) {
                    //登录成功
                    UserInfoModel ui = data.data;
                    SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
                    // 获得Preferences
                    SharedPreferences.Editor editor = sp.edit(); // 获得Editor
                    editor.putString(USERID, email); // 将用户的帐号存入Preferences
                    editor.putString(PASSWORD,password); // 将密码存入Preferences
                    //Toast.makeText(x.app(), data.data.pwd, Toast.LENGTH_LONG).show();
                    if (PreferenceUtil.saveUserInfo(_this, ui)) {
                        Toast.makeText(x.app(), "登录成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, ContentActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    }
                } else {
                    Toast.makeText(x.app(), data.msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }

        });
    }

    @Event(value = R.id.email_regist_button)
    private void onRegistClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }




}
