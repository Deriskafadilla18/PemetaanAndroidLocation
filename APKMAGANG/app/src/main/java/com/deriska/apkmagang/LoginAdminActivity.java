package com.deriska.apkmagang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class LoginAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_user, et_pass;
    private Button btn_login, btn_loginusr;
    private ProgressBar loading;
    private Spinner spinner;
    private static String URL_LOGIN = "http://192.168.110.121/pemetaan/api/login_admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        et_user     = (EditText) findViewById(R.id.username_login);
        et_pass     = (EditText) findViewById(R.id.password_login);
        btn_login   = (Button) findViewById(R.id.btn_login);
        btn_loginusr = (Button) findViewById(R.id.btn_loginusr);
        loading     = (ProgressBar) findViewById(R.id.progress_login);
//        spinner = (Spinner)findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usertype, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        btn_loginusr.setOnClickListener(this);
        btn_login.setOnClickListener(this);
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String item = spinner.getSelectedItem().toString();
//                if (et_user.getText().toString().equals("admin") && et_pass.getText().toString().equals("admin") && item.equals("admin"))
//                    Intent intent = new Intent(LoginadmActivity.this, AdminActivity.class);
//                startActivity(intent);
//
//            }else if(et_user.getText().toString().equals("admin") && et_pass.getText().toString().equals("admin") && item.equals("user")){
//                Intent intent = new Intent(LoginadmActivity.this, ProfileActivity.class);
//                startActivity(intent);
//            }else {
//                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//        }
//        });

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
        } else if (v == btn_loginusr) {
            Intent intent = new Intent(LoginAdminActivity.this, LoginActivity.class);
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
                            String id_admin   = jsonObject.getString("id_admin");
                            if (value.equals("1")){

                                SharedPreferences sharedPreferences = LoginAdminActivity.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(getString(R.string.PREF_NIK), id_admin);
                                editor.commit();

                                Toast.makeText(LoginAdminActivity.this, "Login Sukses!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginAdminActivity.this, AdminPenduduk.class);
                                startActivity(intent);

                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(LoginAdminActivity.this, "Gagal Login! " + pesan, Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginAdminActivity.this, "Login Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginAdminActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
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
