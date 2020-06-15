package com.ibnux.rencanaperjalanan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ibnux.locationpicker.DirectionActivity;
import com.ibnux.locationpicker.LocationPickerActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivityForResult(new Intent(this, LocationPickerActivity.class),4268);

        DirectionActivity.goTo("-6.3763443","106.7190438",this);

        List<String> lokasi = new ArrayList<>();
        lokasi.add("-6.3763443,106.7190438");
        lokasi.add("-6.3740574,106.6355415");
        lokasi.add("-6.3455664,106.7641159");
        DirectionActivity.goTo(lokasi,this);
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