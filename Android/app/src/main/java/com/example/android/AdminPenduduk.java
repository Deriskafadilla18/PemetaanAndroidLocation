package com.example.android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

public class AdminPenduduk extends AppCompatActivity implements DeleteDataDialog.DeleteDataDialogListener{

    Button createData, updateData, deleteData;
    RecyclerView recyclerView;
    ListView listView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<Data> arrayList = new ArrayList<>();
    String DATA_JSON_STRING, data_json_string;
    ProgressDialog progressDialog;
    int countData = 0;
    String[] daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_penduduk);

        recyclerView = (RecyclerView) findViewById(R.id.list_data);
//        createData = (Button) findViewById(R.id.btn_addData);
//        updateData = (Button) findViewById(R.id.btn_editData);
        deleteData = (Button) findViewById(R.id.btn_deleteData);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(AdminPenduduk.this);

//        // Read Data
        getJSON();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                readDataFromServer();
            }
        }, 1000);

        // Create Data
//        createData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCreateDialog();
//            }
//        });
//
//        // Update Data
//        updateData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openUpdateDialog();
//            }
//        });
//
        // Delete Data
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });
    }

        public void openDeleteDialog() {
        DeleteDataDialog deleteDataDialog = new DeleteDataDialog();
        deleteDataDialog.show(getSupportFragmentManager(), "delete dialog");
    }
//
//    public void openUpdateDialog() {
//        UpdateDataDialog updateDataDialog = new UpdateDataDialog();
//        updateDataDialog.show(getSupportFragmentManager(), "update dialog");
//    }
//
//    public void openCreateDialog() {
//        CreateDataDialog createDataDialog = new CreateDataDialog();
//        createDataDialog.show(getSupportFragmentManager(), "create dialog");
//    }
//
    public void deleteDataToServer(final String nik) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            getJSON();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_DELETE_URL,
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

//    public void updateDataToServer(final String id, final String nama) {
//        if (checkNetworkConnection()) {
//            progressDialog.show();
//            getJSON();
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_PUT_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                getJSON();
//                                JSONObject jsonObject = new JSONObject(response);
//                                String resp = jsonObject.getString("server_response");
//                                if (resp.equals("OK")) {
//                                    getJSON();
//                                    Toast.makeText(getApplicationContext(), "Berhasil update Data", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    getJSON();
//                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    getJSON();
//                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("id", id);
//                    params.put("nama", nama);
//                    return params;
//                }
//            };
//
//            VolleySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
//
//            getJSON();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    readDataFromServer();
//                    progressDialog.cancel();
//                }
//            }, 2000);
//        } else {
//            Toast.makeText(getApplicationContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void createDataToServer(final String nama) {
//        if (checkNetworkConnection()) {
//            progressDialog.show();
//            getJSON();
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_POST_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                getJSON();
//                                JSONObject jsonObject = new JSONObject(response);
//                                String resp = jsonObject.getString("server_response");
//                                if (resp.equals("OK")) {
//                                    getJSON();
//                                    Toast.makeText(getApplicationContext(), "Berhasil Menambahkan Data", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    getJSON();
//                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    getJSON();
//                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("nama", nama);
//                    return params;
//                }
//            };
//
//            VolleySingleton.getInstance(AdminPenduduk.this).addToRequestQue(stringRequest);
//
//            getJSON();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    readDataFromServer();
//                    progressDialog.cancel();
//                }
//            }, 2000);
//        } else {
//            Toast.makeText(getApplicationContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show();
//        }
//    }

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
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void getJSON() {
        new BackgroundTask().execute();
    }

//    @Override
//    public void post(String name) {
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        createDataToServer(name);
//    }
//
//    @Override
//    public void update(String id, String nama) {
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        updateDataToServer(id, nama);
//    }
//
    @Override
    public void delete(String id) {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        deleteDataToServer(id);
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

//    public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3){
//        final String selection = daftar[arg2];
//        final CharSequence[] dialogitem = {"Lihat Komentar", "Edit Komentar","Delete Komentar"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPenduduk.this);
//        builder.setTitle("Pilihan");
//        builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item){
//                switch(item){
//                    case 0:
//                        Intent i = new Intent(getApplicationContext(),AdminLihatActivity.class);
//                        i.putExtra("nama",selection);
//                        startActivity(i);
//                        break;
////                    case 1:
////                        Intent in = new Intent(getApplicationContext(),UpdateActivity.class);
////                        in.putExtra("nama",selection);
////                        startActivity(in);
////                        break;
////                    case 2:
////                        SQLiteDatabase db = database.getWritableDatabase();
////                        db.execSQL("delete from protokol where nama = '" + selection + "'");
////                        RefreshList();
////                        break;
//
//                }
//            }
//        });
//        builder.create().show();
//    }
}
