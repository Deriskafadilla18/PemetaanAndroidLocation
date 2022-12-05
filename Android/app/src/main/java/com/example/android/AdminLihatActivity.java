package com.example.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminLihatActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView isi_kel, tgl_kel, status, admin;
    private Button btn_ubah, btn_hapus;
    private String id_keluhan;
    private static String BASE_URL = "http://192.168.43.238/sikemas/api/get_keluhan_id";
    private static String URL_HAPUS = "http://192.168.43.238/sikemas/api/hapus_keluhan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lihat);
//
//        isi_kel = (TextView) findViewById(R.id.tv_isi_lihat);
//        tgl_kel = (TextView) findViewById(R.id.tv_tgl_lihat);
//        status  = (TextView) findViewById(R.id.tv_status_lihat);
//        admin   = (TextView) findViewById(R.id.tv_admin_lihat);
        btn_ubah = (Button) findViewById(R.id.btn_keluhan_form_ubah);
        btn_hapus = (Button) findViewById(R.id.btn_keluhan_hapus);

        Intent intent = getIntent();
        id_keluhan = intent.getStringExtra("id_keluhan");

        get_keluhan_id(id_keluhan);

        btn_hapus.setOnClickListener(this);
        btn_ubah.setOnClickListener(this);
    }

    private void get_keluhan_id(final String id_keluhan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String value = jsonObject.getString("value");
                            String pesan = jsonObject.getString("pesan");
                            JSONObject hasil = jsonObject.getJSONObject("hasil");
                            if (value.equals("1")){

                                if (hasil.getString("status_verif").equalsIgnoreCase("Sudah Diverifikasi")
                                        || hasil.getString("status_verif").equalsIgnoreCase("Verifikasi Ditolak")) {
                                    btn_hapus.setVisibility(View.GONE);
                                    btn_ubah.setVisibility(View.GONE);
                                }

                                isi_kel.setText(hasil.getString("isi_keluhan").trim());
                                tgl_kel.setText(hasil.getString("tgl_keluhan").trim());
                                status.setText(hasil.getString("status_verif").trim());
                                admin.setText(hasil.getString("verifikasi").trim());
                            } else {
                                Toast.makeText(AdminLihatActivity.this, pesan, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminLihatActivity.this, "Data Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminLihatActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_keluhan", id_keluhan);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_ubah) {
            Intent intent = new Intent(AdminLihatActivity.this, AdminUbahActivity.class);
            intent.putExtra("id_keluhan", id_keluhan);
            intent.putExtra("isi_keluhan", isi_kel.getText().toString().trim());
            startActivity(intent);
        } else if (v == btn_hapus) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hapus Keluhan");
            builder.setMessage("Anda Yakin ingin Menghapusnya?");
            builder.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    hapus_keluhan(id_keluhan);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void hapus_keluhan(final String id_keluhan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_HAPUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String value = jsonObject.getString("value");
                            String pesan = jsonObject.getString("pesan");
                            if (value.equals("1")){
                                Toast.makeText(AdminLihatActivity.this, pesan, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AdminLihatActivity.this, AdminActivity.class));
                            } else {
                                Toast.makeText(AdminLihatActivity.this, pesan, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminLihatActivity.this, "Data Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminLihatActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_keluhan", id_keluhan);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
