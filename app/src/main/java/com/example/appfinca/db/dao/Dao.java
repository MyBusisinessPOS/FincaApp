package com.example.appfinca.db.dao;

import com.example.appfinca.db.DBHelper;

import com.example.appfinca.db.bean.Bean;
import com.example.appfinca.db.bean.DaoSession;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

public class Dao {


    protected AbstractDao dao;
    protected DaoSession daoSession;
    public static DaoSession daoExternalSession;

    public Dao(final String daoName) {

        if (daoExternalSession == null) {
            daoSession = DBHelper.getSingleton().getDaoSession();
        } else {
            daoSession = daoExternalSession;
        }

        switch (daoName) {

            case "GestacionBean":
                dao = daoSession.getGestacionBeanDao();
                break;

            case "InventarioBean":
                dao = daoSession.getInventarioBeanDao();
                break;

            case "LecheBean":
                dao = daoSession.getLecheBeanDao();
                break;

            case "LoteBean":
                dao = daoSession.getLoteBeanDao();
                break;

            case "PesoBean":
                dao =daoSession.getPesoBeanDao();
                break;


            case "PartoBean":
                dao =daoSession.getPartoBeanDao();
                break;

            case "UsuarioBean":
                dao =daoSession.getUsuarioBeanDao();
                break;

        }
    }

    public List<Bean> list() {
        return dao.loadAll();
    }

    public void insert(Bean bean) {
        dao.insert(bean);
    }

    public void delete(Bean bean) {
        dao.delete(bean);
    }

    public void beginTransaction() {
        dao.getDatabase().beginTransaction();
    }

    final public static void beginExternalTransaction() {
        daoExternalSession = DBHelper.getSingleton().getDaoSession();
        daoExternalSession.getDatabase().beginTransaction();
    }

    final public static void commitExternalTransaction() {
        daoExternalSession.getDatabase().setTransactionSuccessful();
        daoExternalSession.getDatabase().endTransaction();
        daoExternalSession = null;
    }

    public void commmit() {
        dao.getDatabase().setTransactionSuccessful();
        dao.getDatabase().endTransaction();
    }

    public Bean getByID(final long id) {
        return (Bean) dao.loadByRowId(id);
    }

    public void clear() {
        dao.deleteAll();
    }

    public void insertAll(List<Bean> list) {
        for (Bean bean : list) {
            this.dao.insert(bean);
        }
    }

    public void save(Bean bean) {
        dao.save(bean);
    }

}
