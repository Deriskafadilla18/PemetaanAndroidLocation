package com.example.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

//    private TextView dash_name, total_kel, belum_kel, sudah_kel, tolak_kel;
    private FloatingActionButton actionButton;
    private static String BASE_URL = "http://192.168.175.68/RestIntern/api/admin";
    private String PREF_NIK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

//        dash_name = (TextView) findViewById(R.id.username_dashboard);
        actionButton = (FloatingActionButton) findViewById(R.id.float_add);

//        SharedPreferences sharedPreferences = AdminActivity.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
//        PREF_NIK = sharedPreferences.getString(getString(R.string.PREF_NIK), "00000000000");
//
//        dashboard(PREF_NIK);

        actionButton.setOnClickListener(this);

    }

//    private void dashboard(final String id_admin) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String value = jsonObject.getString("value");
//                            String pesan = jsonObject.getString("pesan");
//                            JSONObject hasil = jsonObject.getJSONObject("hasil");
//                            if (value.equals("1")){
//                                dash_name.setText(hasil.getString("nama").trim());
////                                total_kel.setText(hasil.getString("total_kel").trim());
////                                belum_kel.setText(hasil.getString("belum_kel").trim());
////                                sudah_kel.setText(hasil.getString("sudah_kel").trim());
////                                tolak_kel.setText(hasil.getString("tolak_kel").trim());
//                            } else {
//                                Toast.makeText(AdminActivity.this, pesan, Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(AdminActivity.this, "Data Error! " + e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(AdminActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id_admin", id_admin);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
//    }

    @Override
    public void onClick(View v) {
        if (v == actionButton) {
            Intent intent = new Intent(AdminActivity.this, AdminLihatActivity.class);
            intent.putExtra("id_admin", PREF_NIK);
            startActivity(intent);
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
                        intent = new Intent(AdminActivity.this, BantuanActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item_logout :
                        // hapus data
                        SharedPreferences sharedPreferences = AdminActivity.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(AdminActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }
}
