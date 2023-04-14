package com.example.appfinca.db.dao;

import com.example.appfinca.db.bean.GestacionBean;
import com.example.appfinca.db.bean.GestacionBeanDao;
import com.example.appfinca.db.bean.UsuarioBean;
import com.example.appfinca.db.bean.UsuarioBeanDao;

import java.util.List;

public class GestacionDao extends Dao{
    public GestacionDao() {
        super("GestacionBean");
    }




    final public List<GestacionBean> getByCodigoAnimal(Long codigo) {
        return dao.queryBuilder()
                .where(GestacionBeanDao.Properties.IdInventario.eq(codigo))
                .list();
    }


    final public GestacionBean animal(Long codigo) {
        final List<GestacionBean> usuarioBeans =dao.queryBuilder()
                .where(GestacionBeanDao.Properties.IdInventario.eq(codigo))
                .list();
        return usuarioBeans.size()>0?usuarioBeans.get(0):null;
    }

}
