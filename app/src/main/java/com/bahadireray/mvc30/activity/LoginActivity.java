package com.bahadireray.mvc30.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bahadireray.mvc30.AppConfig;
import com.bahadireray.mvc30.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etUsername,etPassword;
    private Button btnLogin,btnRegister,btnForgotPass;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginCheck();
        setContentView(R.layout.activity_login);
        init();

    }

    private void loginCheck() {
        sharedPref = this.getSharedPreferences("isLoginCheck", Context.MODE_PRIVATE);
        String id = sharedPref.getString("userId", null);

        if (id !=null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }
    private void init() {
        etUsername=(EditText) findViewById(R.id.etUsername);
        etPassword=(EditText) findViewById(R.id.etPassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnRegister=(Button) findViewById(R.id.btnRegister);
        btnForgotPass=(Button) findViewById(R.id.btnForgotPassword);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:{
                String username=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                if(!username.isEmpty()&&!password.isEmpty()){
                    checkLogin(username,password);
                }else {
                    Toast.makeText(getApplicationContext(), "Kullanıcı adi ve Şifre boş olamaz", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.btnRegister:{
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            }
            case R.id.btnForgotPassword:{

                break;
            }
        }
    }
    String message;

    private void checkLogin(final String email, final String password) {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);

                String status = jObj.getString("status");
                message = jObj.getString("message");

                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("userId",jObj.getJSONObject("data").getJSONObject("user").getString("id"));

                    editor.commit(); //Kayıt

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Error 400", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email);
                parameters.put("password", password);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);
    }

}
