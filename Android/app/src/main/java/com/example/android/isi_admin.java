package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class isi_admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_admin);
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
                        intent = new Intent(isi_admin.this, BantuanActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item_logout :
                        // hapus data
                        SharedPreferences sharedPreferences = isi_admin.this.getSharedPreferences("SIKEMAS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(isi_admin.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        intent = new Intent(isi_admin.this, LoginAdminActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }
}
