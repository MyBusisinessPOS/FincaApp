package com.example.appfinca.db.dao;

import com.example.appfinca.db.bean.UsuarioBean;
import com.example.appfinca.db.bean.UsuarioBeanDao;

import org.greenrobot.greendao.query.CountQuery;

import java.util.List;

public class UsuarioDao extends Dao{
    public UsuarioDao() {
        super("UsuarioBean");
    }

    final public int getConfig() {
        final CountQuery<UsuarioBean> query = dao.queryBuilder().buildCount();
        return (int)query.count();
    }

    final public UsuarioBean getUsuario(final String email) {
        final List<UsuarioBean> usuarioBeans =dao.queryBuilder()
                .where(UsuarioBeanDao.Properties.Email.eq(email.trim()))
                .list();
        return usuarioBeans.size()>0?usuarioBeans.get(0):null;
    }

    final public UsuarioBean login(final String email, final String password) {
        final List<UsuarioBean> usuarioBeans =dao.queryBuilder()
                .where(UsuarioBeanDao.Properties.Email.eq(email.trim()), UsuarioBeanDao.Properties.Password.eq(password))
                .list();
        return usuarioBeans.size()>0?usuarioBeans.get(0):null;
    }
}
