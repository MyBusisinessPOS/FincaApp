package com.example.appfinca.db.dao;

import com.example.appfinca.db.bean.GestacionBean;
import com.example.appfinca.db.bean.GestacionBeanDao;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.InventarioBeanDao;

import java.util.List;

public class InventarioDao extends Dao{
    public InventarioDao() {
        super("InventarioBean");
    }
    
    final public InventarioBean getByCodigoAnimal(final String codigo) {
        final List<InventarioBean> inventarioBeans =dao.queryBuilder()
                .where(InventarioBeanDao.Properties.Codigo_animal.eq(codigo.trim()))
                .list();
        return inventarioBeans.size()>0?inventarioBeans.get(0):null;
    }


    final public List<InventarioBean> getAllLote(Long codigo) {
        return dao.queryBuilder()
                .where(InventarioBeanDao.Properties.IdLote.eq(codigo))
                .list();
    }



}
