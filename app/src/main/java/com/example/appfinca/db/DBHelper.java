package com.example.appfinca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.appfinca.db.bean.DaoMaster;
import com.example.appfinca.db.bean.DaoSession;

public class DBHelper {
    private SQLiteDatabase db;
    private DaoSession daoSession;
    private static DBHelper daoHelperSingleton;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;

    private DBHelper(){

        //Prevent form the reflection api.
        if (daoHelperSingleton != null){
            throw new RuntimeException("Use getSingleton() method to get the single instance of this class.");
        }
    }

    final public void init(final Context context, final String dbName){
        // do this once, for example in your Application class
        helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    final public static DBHelper getSingleton(){
        if (daoHelperSingleton == null){
            daoHelperSingleton = new DBHelper();
        }
        return daoHelperSingleton;
    }

    @Override
    protected Object clone() {
        throw new RuntimeException("Use getSingleton() method to get the single instance of this class.");
    }

    public DaoSession getDaoSession() {
        if(daoSession != null)
            return daoSession;
        return null;
    }
}
