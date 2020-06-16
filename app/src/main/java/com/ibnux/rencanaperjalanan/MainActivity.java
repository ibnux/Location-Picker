package com.ibnux.rencanaperjalanan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibnux.locationpicker.DirectionActivity;
import com.ibnux.locationpicker.GetDistanceActivity;
import com.ibnux.locationpicker.LocationPickerActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button1,button2,button3,button4,button5,button6;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResult = findViewById(R.id.txtResult);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==button1){
            startActivityForResult(new Intent(this, LocationPickerActivity.class),4268);
        }else if(v==button2){
            DirectionActivity.goTo("-6.3763443","106.7190438",this);
        }else if(v==button3){
            List<String> lokasi = new ArrayList<>();
            lokasi.add("-6.3763443,106.7190438");
            lokasi.add("-6.3740574,106.6355415");
            DirectionActivity.goTo(lokasi,this);
        }else if(v==button4){
            List<String> lokasi = new ArrayList<>();
            lokasi.add("-6.3763443,106.7190438");
            lokasi.add("-6.3740574,106.6355415");
            lokasi.add("-6.3455664,106.7641159");
            DirectionActivity.goTo(lokasi,this);
        }else if(v==button5){
            GetDistanceActivity.disTanceOf("-6.3763443","106.7190438",
                "-6.3455664","106.7641159",
                8264, this);
        }else if(v==button6){
            List<String> lokasi = new ArrayList<>();
            lokasi.add("-6.3763443,106.7190438");
            lokasi.add("-6.3740574,106.6355415");
            GetDistanceActivity.disTanceOf("-6.3455664","106.7641159",lokasi,8264,this);
        }
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
                txtResult.setText("Result \nLatitude: "+latitude+"\nLongitude: "+longitude);
            }else

            if (requestCode == 8264){
                String meter = data.getStringExtra("meters");
                String times = data.getStringExtra("times");
                LocationPickerActivity.log("Rsult "+meter+" | "+times);

                txtResult.setText("Result \n"+meter+" meters\n"+times);
            }
        }

    }
}