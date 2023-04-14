package com.example.appfinca;

import android.app.Application;

import com.example.appfinca.db.DBHelper;

public class App extends Application {


    private DBHelper daoHelper;

    private static App _INSTANCE = null;

    public static App getInstance() {
        return _INSTANCE;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        _INSTANCE = this;

        /*** ----- Obtenemos el singleton ------ ****/
        daoHelper = DBHelper.getSingleton();

        /** Inicia el Helper **/
        daoHelper.init(_INSTANCE, "finca6_db");

    }
}
