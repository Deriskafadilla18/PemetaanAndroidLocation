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

public class AdminTambah extends AppCompatActivity implements View.OnClickListener {
    private EditText nik_add, nama, tempat_lahir, tanggal_lahir;
    private Button btn_tambah;
    private TextView tv_back;
    private ProgressBar loading;
    private static String URL_ADD = "http://192.168.110.121/pemetaan/api/add_penduduk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tambah);

        nik_add          = (EditText) findViewById(R.id.nik_add);
        nama             = (EditText) findViewById(R.id.nama);
        tempat_lahir     = (EditText) findViewById(R.id.tempat_lahir);
        tanggal_lahir    = (EditText) findViewById(R.id.tanggal_lahir);
        btn_tambah       = (Button) findViewById(R.id.btn_tambah);
        tv_back          = (TextView) findViewById(R.id.tv_back);
        loading          = (ProgressBar) findViewById(R.id.progress_register);

        tv_back.setOnClickListener(this);
        btn_tambah.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == tv_back) {
            Intent intent = new Intent(AdminTambah.this, AdminPenduduk.class);
            startActivity(intent);
        } else if (view == btn_tambah) {
            Register();
        }
    }

    private void Register() {
        loading.setVisibility(View.VISIBLE);
        btn_tambah.setVisibility(View.GONE);

        final String nik = this.nik_add.getText().toString().trim();
        final String nama = this.nama.getText().toString().trim();
        final String tempat_lahir = this.tempat_lahir.getText().toString().trim();
        final String tanggal_lahir = this.tanggal_lahir.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String value = jsonObject.getString("value");
                            String pesan = jsonObject.getString("pesan");
                            if (value.equals("1")){
                                Toast.makeText(AdminTambah.this, pesan, Toast.LENGTH_SHORT).show();
                                Intent berhasil = new Intent(AdminTambah.this, tambahSuccess.class);
                                startActivity(berhasil);
                                loading.setVisibility(View.GONE);
                                btn_tambah.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(AdminTambah.this, pesan, Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_tambah.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminTambah.this, "Registrasi Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_tambah.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminTambah.this, "Registrasi Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_tambah.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("nama", nama);
                params.put("tempat_lahir", tempat_lahir);
                params.put("tanggal_lahir", tanggal_lahir);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}