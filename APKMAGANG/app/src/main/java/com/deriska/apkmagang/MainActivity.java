package com.deriska.apkmagang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView nik, nama, ttl, jk, agama;
    private static String BASE_URL = "http://192.168.110.121/pemetaan/api/profile";
    private String PREF_NIK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        nik     = (TextView) findViewById(R.id.nik_profile);
        nama    = (TextView) findViewById(R.id.nama_profile);
        ttl     = (TextView) findViewById(R.id.ttl_profile);
        jk      = (TextView) findViewById(R.id.jk_profile);
        agama   = (TextView) findViewById(R.id.agama_profile);

        SharedPreferences sharedPreferences = com.deriska.apkmagang.MainActivity.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
        PREF_NIK = sharedPreferences.getString(getString(R.string.PREF_NIK), "00000000000");

        profile(PREF_NIK);
    }

    private void profile(final String PREF_NIK) {
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
                                nik.setText(hasil.getString("nik").trim());
                                nama.setText(hasil.getString("nama").trim());
                                ttl.setText(hasil.getString("ttl").trim());
                                jk.setText(hasil.getString("jk").trim());
                                agama.setText(hasil.getString("agama").trim());
                            } else {
                                Toast.makeText(com.deriska.apkmagang.MainActivity.this, pesan, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(com.deriska.apkmagang.MainActivity.this, "Data Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(com.deriska.apkmagang.MainActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nik", PREF_NIK);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
