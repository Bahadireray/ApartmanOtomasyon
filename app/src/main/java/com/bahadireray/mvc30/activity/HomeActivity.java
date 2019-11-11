package com.bahadireray.mvc30.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import com.bahadireray.mvc30.adapter.CustomRecyclerViewAdapter;
import com.bahadireray.mvc30.model.MyModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogout;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPref;
    private LinearLayoutManager linearLayoutManager;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;
    private List<Object> myList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        getData();
    }

    private void init() {

        btnLogout=(Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);


        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this);
        recyclerView.setAdapter(customRecyclerViewAdapter);






    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnLogout: {
                sharedPref = this.getSharedPreferences("isLoginCheck", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("userId",null);
                editor.commit();

                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();
                break;
            }
        }
    }



    String message;
    public void getData() {


        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.Url_GETDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Boolean status = jObj.getBoolean("status");
                    message = jObj.getString("message");

                    if (status) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


                        JSONObject dataArray = jObj.getJSONObject("data");



                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<MyModel>>() {
                        }.getType();

                        myList = gson.fromJson(String.valueOf(dataArray.getJSONArray("operation")), listType);

                        customRecyclerViewAdapter.setObjectFeed((List<Object>) myList);
                        customRecyclerViewAdapter.notifyDataSetChanged();



                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Error 400", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(request);
    }
}
