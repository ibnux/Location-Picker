package com.ibnux.rencanaperjalanan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ibnux.locationpicker.MapsPickerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivityForResult(new Intent(this, MapsPickerActivity.class),4268);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode == 4268){
                String latitude = data.getStringExtra("lat");
                String longitude = data.getStringExtra("lon");
                //to double
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);
            }
        }

    }
}