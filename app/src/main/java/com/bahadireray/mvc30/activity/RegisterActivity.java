package com.bahadireray.mvc30.activity;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {



    private EditText etUsername,etPassword,etName,etSurname,etHomeId;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:{
                String username=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                String name=etName.getText().toString();
                String surname=etSurname.getText().toString();
                if (!surname.isEmpty()&&!password.isEmpty()&&!name.isEmpty()&&!surname.isEmpty()) {

                    register(username,password,name,surname);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Tüm Alanları Doldurunuz!",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    private void init() {


        etUsername=(EditText) findViewById(R.id.etUsername);
        etPassword=(EditText) findViewById(R.id.etPassword);
        etName=(EditText) findViewById(R.id.etName);
        etSurname=(EditText) findViewById(R.id.etSurname);
        etHomeId=(EditText) findViewById(R.id.etHomeId);
        btnRegister=(Button) findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(this);

    }

    private String message;
    private void register(final String email, final String password, final String name, final String surname) {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);

                    String status = jObj.getString("status");
                    message = jObj.getString("message");
//APİ karşılığında değişebilecek kısım
                    if (status.equals("success")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
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
                parameters.put("name", name);
                parameters.put("surname", surname);


                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(request);
    }



}
