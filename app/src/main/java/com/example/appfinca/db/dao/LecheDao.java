package com.example.appfinca.db.dao;

import com.example.appfinca.db.bean.GestacionBean;
import com.example.appfinca.db.bean.GestacionBeanDao;
import com.example.appfinca.db.bean.LecheBean;
import com.example.appfinca.db.bean.LecheBeanDao;

import java.util.List;

public class LecheDao extends Dao{
    public LecheDao() {
        super("LecheBean");
    }

    final public List<LecheBean> getByCodigoAnimal(Long codigo) {
        return dao.queryBuilder()
                .where(LecheBeanDao.Properties.InventarioId.eq(codigo))
                .list();
    }




}
