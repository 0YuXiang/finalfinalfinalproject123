package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class more_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String number = extras.getString("number");
            String date = extras.getString("date");
            String departuer = extras.getString("departure");
            String arrival = extras.getString("arrival");

            // 假设你已经在布局文件中定义了对应的 TextView 对象
            TextView tvLocationsName = findViewById(R.id.number);
            TextView tvLocationName = findViewById(R.id.date);
            TextView tvDataTime = findViewById(R.id.departure);
            TextView tvValue = findViewById(R.id.arrival);

            // 将数据设置到 TextView 对象中
            tvLocationsName.setText(number);
            tvLocationName.setText("日期: " + date);
            tvDataTime.setText("出發地: " + departuer);
            tvValue.setText("目的地: " + arrival);
        }
    }

    public void goBack(View view) {
        finish();
    }
}