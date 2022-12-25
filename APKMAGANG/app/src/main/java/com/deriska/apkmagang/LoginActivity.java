package com.deriska.apkmagang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_user, et_pass;
    private Button btn_login,btn_loginadm;
    private TextView tv_register, tv_bantuan;
    private ProgressBar loading;
    private static String URL_LOGIN = "https://muskier-mill.000webp.com/pemetaan/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_user     = (EditText) findViewById(R.id.username_login);
        et_pass     = (EditText) findViewById(R.id.password_login);
        btn_login   = (Button) findViewById(R.id.btn_login);
        btn_loginadm   = (Button) findViewById(R.id.btn_loginadm);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_bantuan  = (TextView) findViewById(R.id.tv_bantuan);
        loading     = (ProgressBar) findViewById(R.id.progress_login);

        tv_register.setOnClickListener(this);
        tv_bantuan.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_loginadm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_login) {
            String user = et_user.getText().toString().trim();
            String pass = et_pass.getText().toString().trim();

            if (!user.isEmpty() && !pass.isEmpty()){
                Login(user, pass);
            } else {
                et_user.setError("Masukkan Username!");
                et_pass.setError("Masukkan Password!");
            }
        } else if (v == tv_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        } else if (v == tv_bantuan) {
            Intent intent = new Intent(LoginActivity.this, BantuanActivity.class);
            startActivity(intent);
        } else if (v == btn_loginadm) {
            Intent intent = new Intent(LoginActivity.this, LoginAdminActivity.class);
            startActivity(intent);
        }
    }

    private void Login(final String user, final String pass) {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String value = jsonObject.getString("value");
                            String pesan = jsonObject.getString("pesan");
                            String nik   = jsonObject.getString("nik");
                            if (value.equals("1")){

                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(getString(R.string.PREF_NIK), nik);
                                editor.commit();

                                Toast.makeText(LoginActivity.this, "Login Sukses!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                startActivity(intent);

                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(LoginActivity.this, "Gagal Login! " + pesan, Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Login Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("pass", pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
