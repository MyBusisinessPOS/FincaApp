package com.example.appfinca.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appfinca.Actividades;
import com.example.appfinca.R;
import com.example.appfinca.db.bean.UsuarioBean;
import com.example.appfinca.db.dao.UsuarioDao;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    private void  validaUsuario(){

        UsuarioDao usuarioDao = new UsuarioDao();

        int registros = usuarioDao.getConfig();
        if (registros == 0){
            UsuarioBean usuarioBean = new UsuarioBean();
            UsuarioDao dao = new UsuarioDao();
            usuarioBean.setEmail("admin@demo.com");
            usuarioBean.setPassword("123");
            usuarioBean.setRecordar(false);
            dao.save(usuarioBean);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        validaUsuario();
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

                    UsuarioDao usuarioDao = new UsuarioDao();
                    UsuarioBean usuarioBean = usuarioDao.getUsuario("admin@demo.com");

                    if (usuarioBean.getRecordar() == true){
                        Actividades.getSingleton(SplashActivity.this, MainActivity.class).muestraActividad();
                    }else {
                        Actividades.getSingleton(SplashActivity.this, LoginActivity.class).muestraActividad();
                    }

                    finish();
                }
            }
        };
        splashThread.start();
    }


}