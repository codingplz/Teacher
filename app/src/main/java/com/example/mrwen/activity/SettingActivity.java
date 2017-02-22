package com.example.mrwen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SettingActivity extends AppCompatActivity {
    private LinearLayout layout_personInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        layout_personInfo=(LinearLayout)findViewById(R.id.layout_personInfo);

        layout_personInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPersonInfo=new Intent(SettingActivity.this,PersonInfoActivity.class);
                startActivity(intentPersonInfo);
            }
        });

    }
}
