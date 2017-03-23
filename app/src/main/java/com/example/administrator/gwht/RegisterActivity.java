package com.example.administrator.gwht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;

import Model.BaseModel;
import Tools.AppApi;
import Tools.PreferenceUtil;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    public static final String SP_INFOS = "SPDATA_Files";
    public static final String USERID = "UserID";
    public static final String PASSWORD = "PassWord";

    // UI references.
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.name)
    private EditText mEmailView;
    @ViewInject(R.id.password)
    private EditText mPasswordView;
    @ViewInject(R.id.password2)
    private EditText mPasswordView2;
    @ViewInject(R.id.email_regist_button)
    private Button registButton;
    @ViewInject(R.id.name)
    private EditText nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

    }

    @Event(value = R.id.email_regist_button)
    private void onRegistClick(View view) {
        //Toast.makeText(x.app(), "注册成功！", Toast.LENGTH_LONG).show();
        attemptRegist();
    }

    private void attemptRegist() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String pwdAgain=mPasswordView2.getText().toString();

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
        if(!isPasswordAgainValid(password,pwdAgain)){
            mPasswordView.setError(getString(R.string.error_invalid_pwdOfBefore));
            focusView = mPasswordView2;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
           // showProgress(true);
            userRegist(email, password);
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
    private boolean isPasswordAgainValid(String password,String pwdAgain) {
        //TODO: Replace this with your own logic
        return password.equals(pwdAgain);
    }



    /** 请求后台进行注册 **/
    private void userRegist(String email, String password) {
        RequestParams requestParams = new RequestParams(AppApi.POST_REGIST_URL);
        Map<String, String> map =PreferenceUtil.getUserInfo(this);
        requestParams.addBodyParameter("userid", map.get("userid"));
        requestParams.addBodyParameter("channelid",map.get("channelid"));
        requestParams.addBodyParameter("password", password);
        requestParams.addBodyParameter("username", email);

        final Context _this = this;
        //指定回调函数的返回类型为JSON
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                BaseModel data = gson.fromJson(result, BaseModel.class);
                if(data.code == 0) {
                    //登录成功
                    Toast.makeText(x.app(), "注册成功！", Toast.LENGTH_LONG).show();
                    RegisterActivity.this.finish();
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
}
