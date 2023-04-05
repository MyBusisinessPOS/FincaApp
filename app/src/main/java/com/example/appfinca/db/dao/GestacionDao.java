package com.example.appfinca.db.dao;

import com.example.appfinca.db.bean.GestacionBean;
import com.example.appfinca.db.bean.GestacionBeanDao;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.InventarioBeanDao;
import com.example.appfinca.db.bean.PartoBean;
import com.example.appfinca.db.bean.PartoBeanDao;

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

}
