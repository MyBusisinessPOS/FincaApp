package com.example.appfinca.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "pesos")
public class PesoBean extends Bean{

    @Id(autoincrement = true)
    private Long id;
    private Date fecha;
    private Double peso;
    private Long idInventario;
    @ToOne(joinProperty = "idInventario")
    private InventarioBean INVENTARIO;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 822117673)
    private transient PesoBeanDao myDao;
    @Generated(hash = 289836622)
    public PesoBean(Long id, Date fecha, Double peso, Long idInventario) {
        this.id = id;
        this.fecha = fecha;
        this.peso = peso;
        this.idInventario = idInventario;
    }
    @Generated(hash = 1339431668)
    public PesoBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getFecha() {
        return this.fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Long getIdInventario() {
        return this.idInventario;
    }
    public void setIdInventario(Long idInventario) {
        this.idInventario = idInventario;
    }
    @Generated(hash = 1572140723)
    private transient Long INVENTARIO__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1675620995)
    public InventarioBean getINVENTARIO() {
        Long __key = this.idInventario;
        if (INVENTARIO__resolvedKey == null
                || !INVENTARIO__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InventarioBeanDao targetDao = daoSession.getInventarioBeanDao();
            InventarioBean INVENTARIONew = targetDao.load(__key);
            synchronized (this) {
                INVENTARIO = INVENTARIONew;
                INVENTARIO__resolvedKey = __key;
            }
        }
        return INVENTARIO;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 5590953)
    public void setINVENTARIO(InventarioBean INVENTARIO) {
        synchronized (this) {
            this.INVENTARIO = INVENTARIO;
            idInventario = INVENTARIO == null ? null : INVENTARIO.getId();
            INVENTARIO__resolvedKey = idInventario;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    public Double getPeso() {
        return this.peso;
    }
    public void setPeso(Double peso) {
        this.peso = peso;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 377589743)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPesoBeanDao() : null;
    }




}
