package com.deriska.apkmagang;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
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

public class ProfileActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Boolean oke = false;
    private TextView nik, nama, ttl, jk, agama, latitude, longitude, altitude, akurasi, alamat;
    private Button btnSimpan, btnCari;
    private FusedLocationProviderClient locationProviderClient;
    private static String BASE_URL = "https://muskier-mill.000webhostapp.com/pemetaan/api/";
    private String PREF_NIK;
//    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        altitude = findViewById(R.id.altitude);
        akurasi = findViewById(R.id.akurasi);
        alamat = findViewById(R.id.alamat);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnCari = findViewById(R.id.btn_find);
        nik = (TextView) findViewById(R.id.nik_profile);
        nama = (TextView) findViewById(R.id.nama_profile);
        ttl = (TextView) findViewById(R.id.ttl_profile);
        jk = (TextView) findViewById(R.id.jk_profile);
        agama = (TextView) findViewById(R.id.agama_profile);

        SharedPreferences sharedPreferences = ProfileActivity.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
        PREF_NIK = sharedPreferences.getString(getString(R.string.PREF_NIK), "00000000000");

        profile(PREF_NIK);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(ProfileActivity.this);
        btnCari.setOnClickListener(v -> {
            getLocation();
        });
        btnSimpan.setOnClickListener(v -> {
            saveLocation();
        });
    }

    private void saveLocation(){
        String lat  = latitude.getText().toString().trim();
        String lon = longitude.getText().toString().trim();
        String al = altitude.getText().toString().trim();
        String ak = akurasi.getText().toString().trim();
        String alm = alamat.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"index_ubah",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String pesan = jsonObject.getString("pesan");
                            Toast.makeText(ProfileActivity.this, pesan, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Data Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", lat);
                params.put("longitude", lon);
                params.put("altitude", al);
                params.put("alamat", alm);
                params.put("nik", PREF_NIK);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void profile(final String PREF_NIK) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String value = jsonObject.getString("value");
                            String pesan = jsonObject.getString("pesan");
                            JSONObject hasil = jsonObject.getJSONObject("hasil");
                            if (value.equals("1")) {
                                nik.setText(hasil.getString("nik").trim());
                                nama.setText(hasil.getString("nama").trim());
                                ttl.setText(hasil.getString("ttl").trim());
                                jk.setText(hasil.getString("jk").trim());
                                agama.setText(hasil.getString("agama").trim());
                            } else {
                                Toast.makeText(ProfileActivity.this, pesan, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Data Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "izin lokasi tidak diaktifkan", Toast.LENGTH_SHORT).show();
            } else {
                getLocation();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        oke = true;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    @SuppressLint("SetTextI18n")
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //get permision
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 10);
        } else {
            //get location
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                        altitude.setText(String.valueOf(location.getAltitude()));
                        akurasi.setText(location.getAccuracy() + "%");
                    } else {
                        Toast.makeText(getApplicationContext(), "Lokasi tidak aktif", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            Intent intent;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_bantuan :
                        intent = new Intent(ProfileActivity.this, BantuanActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item_logout :
                        // hapus data
                        SharedPreferences sharedPreferences = ProfileActivity.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(ProfileActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }
}

