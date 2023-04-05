package com.example.appfinca.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appfinca.Actividades;
import com.example.appfinca.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    //Abrimos la actividad del login

                    Actividades.getSingleton(SplashActivity.this, MainActivity.class).muestraActividad();
                    finish();
                }
            }
        };
        splashThread.start();
    }
}