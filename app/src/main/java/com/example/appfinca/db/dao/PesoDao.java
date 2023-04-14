package com.example.appfinca.db.dao;

import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.InventarioBeanDao;
import com.example.appfinca.db.bean.PartoBean;
import com.example.appfinca.db.bean.PartoBeanDao;
import com.example.appfinca.db.bean.PesoBean;
import com.example.appfinca.db.bean.PesoBeanDao;

import java.util.List;

public class PesoDao extends Dao {
    public PesoDao() {
        super("PesoBean");
    }

    final public List<PesoBean> getByCodigoAnimal(Long codigo) {
        return dao.queryBuilder()
                .where(PesoBeanDao.Properties.IdInventario.eq(codigo))

                .list();
    }


    final public PesoBean animal(Long codigo) {
        final List<PesoBean> usuarioBeans =dao.queryBuilder()
                .where(PesoBeanDao.Properties.IdInventario.eq(codigo))
                .list();
        return usuarioBeans.size()>0?usuarioBeans.get(0):null;
    }
}
