package com.deriska.apkmagang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class tambahSuccess extends AppCompatActivity {

    private Button btn_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_success);

        btn_regist = (Button) findViewById(R.id.btn_goto_admin);

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tambahSuccess.this, AdminPenduduk.class));
            }
        });

    }
}
