package com.deriska.apkmagang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminPenduduk extends AppCompatActivity implements DeleteDataDialog.DeleteDataDialogListener, UpdateDataDialog.UpdateDataDialogListener{

    ScriptGroup.Binding binding;
    Button createData, updateData, deleteData;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<Data> arrayList = new ArrayList<>();
    AdapterView.OnItemClickListener mListener;
    String DATA_JSON_STRING, data_json_string;
    ProgressDialog progressDialog;
    int countData = 0;
    private static String URL_DEL = "http://192.168.110.121/pemetaan/api/hapus_penduduk";
    private static String URL_UBAH = "http://192.168.110.121/pemetaan/api/ubah_penduduk";


//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = (AdapterView.OnItemClickListener) listener;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_penduduk);

        recyclerView = (RecyclerView) findViewById(R.id.list_data);
        createData = (Button) findViewById(R.id.btn_addData);
        updateData = (Button) findViewById(R.id.btn_updateData);
        deleteData = (Button) findViewById(R.id.btn_deleteData);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(AdminPenduduk.this);

        createData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPenduduk.this, AdminTambah.class);
                startActivity(intent);
            }
        });

        // Read Data
        getJSON();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                readDataFromServer();
            }
        }, 1000);

        // Delete Data
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });

        // Update Data
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog();
            }
        });
    }

    public void openDeleteDialog() {
        DeleteDataDialog deleteDataDialog = new DeleteDataDialog();
        deleteDataDialog.show(getSupportFragmentManager(), "delete dialog");
    }

    public void openUpdateDialog() {
        UpdateDataDialog updateDataDialog = new UpdateDataDialog();
        updateDataDialog.show(getSupportFragmentManager(), "update dialog");
    }

    public void readDataFromServer() {
        if (checkNetworkConnection()) {
            arrayList.clear();
            try {
                JSONObject object = new JSONObject(data_json_string);
                JSONArray serverResponse = object.getJSONArray("server_response");
                String nik, nama;

                while (countData < serverResponse.length()) {
                    JSONObject jsonObject = serverResponse.getJSONObject(countData);
                    nik = jsonObject.getString("nik");
                    nama = jsonObject.getString("nama");

                    arrayList.add(new Data(nik, nama));
                    countData++;
                }
                countData = 0;

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        adapter listAdapter = new adapter(AdminPenduduk.this,arrayList);
//
//        binding.listview.setAdapter(listAdapter);
//        binding.listview.setClickable(true);
//        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent i = new Intent(AdminPenduduk.this,Data.class);
//                i.putExtra("name",name[position]);
//                i.putExtra("phone",phoneNo[position]);
//                i.putExtra("country",country[position]);
//                i.putExtra("imageid",imageId[position]);
//                startActivity(i);
//
//            }
//        });


    }

    public void deleteDataToServer(final String nik) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            getJSON();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DEL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                getJSON();
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("OK")) {
                                    getJSON();
                                    Toast.makeText(getApplicationContext(), "Berhasil delete Data", Toast.LENGTH_SHORT).show();
                                } else {
                                    getJSON();
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getJSON();
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nik", nik);
                    return params;
                }
            };

            VolleySingleton.getInstance(AdminPenduduk.this).addToRequestQue(stringRequest);

            getJSON();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    readDataFromServer();
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDataToServer(final String nik, final String nama, final String alamat) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            getJSON();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UBAH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                getJSON();
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("OK")) {
                                    getJSON();
                                    Toast.makeText(getApplicationContext(), "Berhasil update Data", Toast.LENGTH_SHORT).show();
                                } else {
                                    getJSON();
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getJSON();
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nik", nik);
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                    return params;
                }
            };

            VolleySingleton.getInstance(AdminPenduduk.this).addToRequestQue(stringRequest);

            getJSON();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    readDataFromServer();
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void getJSON() {
        new BackgroundTask().execute();
    }

    @Override
    public void delete(String nik) {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        deleteDataToServer(nik);
    }
    @Override
    public void update(String nik, String nama, String alamat) {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        updateDataToServer(nik, nama, alamat);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = DbContract.SERVER_GET_URL;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((DATA_JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(DATA_JSON_STRING + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return  stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            data_json_string = result;
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
                        intent = new Intent(AdminPenduduk.this, BantuanActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item_logout :
                        // hapus data
                        SharedPreferences sharedPreferences = AdminPenduduk.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(AdminPenduduk.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AdminPenduduk.this, LoginAdminActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

}