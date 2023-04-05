package com.example.appfinca.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "partos")
public class PartoBean extends Bean{

    @Id(autoincrement = true)
    private Long id;
    private Date fecha;
    private String registro;

    private Long inventarioId;
    @ToOne(joinProperty = "inventarioId")
    private InventarioBean INVENTARIO;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 200511876)
    private transient PartoBeanDao myDao;
    @Generated(hash = 877347761)
    public PartoBean(Long id, Date fecha, String registro, Long inventarioId) {
        this.id = id;
        this.fecha = fecha;
        this.registro = registro;
        this.inventarioId = inventarioId;
    }
    @Generated(hash = 647547180)
    public PartoBean() {
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
    public String getRegistro() {
        return this.registro;
    }
    public void setRegistro(String registro) {
        this.registro = registro;
    }
    public Long getInventarioId() {
        return this.inventarioId;
    }
    public void setInventarioId(Long inventarioId) {
        this.inventarioId = inventarioId;
    }
    @Generated(hash = 1572140723)
    private transient Long INVENTARIO__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 373506528)
    public InventarioBean getINVENTARIO() {
        Long __key = this.inventarioId;
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
    @Generated(hash = 1468738530)
    public void setINVENTARIO(InventarioBean INVENTARIO) {
        synchronized (this) {
            this.INVENTARIO = INVENTARIO;
            inventarioId = INVENTARIO == null ? null : INVENTARIO.getId();
            INVENTARIO__resolvedKey = inventarioId;
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1145190678)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPartoBeanDao() : null;
    }



}
