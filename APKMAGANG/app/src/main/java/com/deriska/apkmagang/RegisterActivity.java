package com.deriska.apkmagang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nik_reg, user_reg, pass_reg, cpass_reg;
    private CheckBox cb_setuju;
    private Button btn_register;
    private TextView tv_login;
    private ProgressBar loading;
    private static String URL_REGIST = "http://192.168.110.121/pemetaan/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nik_reg      = (EditText) findViewById(R.id.nik_register);
        user_reg     = (EditText) findViewById(R.id.username_register);
        pass_reg     = (EditText) findViewById(R.id.password_register);
        cpass_reg    = (EditText) findViewById(R.id.cpassword_register);
        cb_setuju    = (CheckBox) findViewById(R.id.cb_aturan);
        btn_register = (Button) findViewById(R.id.btn_register);
        tv_login     = (TextView) findViewById(R.id.tv_login);
        loading      = (ProgressBar) findViewById(R.id.progress_register);

        tv_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_login) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (v == btn_register) {
            String nik  = nik_reg.getText().toString().trim();
            String user = user_reg.getText().toString().trim();
            String pass = pass_reg.getText().toString().trim();
            String cpass = cpass_reg.getText().toString().trim();

            if (!nik.isEmpty() && !user.isEmpty() && !pass.isEmpty() && !cpass.isEmpty()){
                if (cb_setuju.isChecked()) {
                    if (pass.equals(cpass)) Register();
                    else cpass_reg.setError("Konfirmasi password salah");
                } else {
                    cb_setuju.setError("Centang checkbox berikut!");
                }
            } else {
                nik_reg.setError("Masukkan NIK!");
                user_reg.setError("Masukkan username!");
                pass_reg.setError("Masukkan password!");
                cpass_reg.setError("Masukkan konfirmasi password!");

            }

        }
    }

    private void Register() {
        loading.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.GONE);

        final String nik = this.nik_reg.getText().toString().trim();
        final String user = this.user_reg.getText().toString().trim();
        final String pass = this.pass_reg.getText().toString().trim();
        final String cpass = this.cpass_reg.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String value = jsonObject.getString("value");
                            String pesan = jsonObject.getString("pesan");
                            if (value.equals("1")){
                                Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                Intent berhasil = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                                startActivity(berhasil);
                                loading.setVisibility(View.GONE);
                                btn_register.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_register.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Registrasi Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_register.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Registrasi Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_register.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("user", user);
                params.put("pass", pass);
                params.put("cpass", cpass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
