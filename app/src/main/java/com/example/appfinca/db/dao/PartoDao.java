package com.example.appfinca.db.dao;

import com.example.appfinca.db.bean.LecheBean;
import com.example.appfinca.db.bean.LecheBeanDao;
import com.example.appfinca.db.bean.PartoBean;
import com.example.appfinca.db.bean.PartoBeanDao;
import com.example.appfinca.db.bean.PesoBean;
import com.example.appfinca.db.bean.PesoBeanDao;

import java.util.List;

public class PartoDao extends Dao{
    public PartoDao() {
        super("PartoBean");
    }

    final public List<PartoBean> getByCodigoAnimal(Long codigo) {
        return dao.queryBuilder()
                .where(PartoBeanDao.Properties.InventarioId.eq(codigo))
                .list();
    }


    final public PartoBean animal(Long codigo) {
        final List<PartoBean> usuarioBeans =dao.queryBuilder()
                .where(PartoBeanDao.Properties.InventarioId.eq(codigo))
                .list();
        return usuarioBeans.size()>0?usuarioBeans.get(0):null;
    }
}
