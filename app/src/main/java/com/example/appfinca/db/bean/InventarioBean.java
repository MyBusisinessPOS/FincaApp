package com.example.appfinca.db.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "inventarios")
public class InventarioBean extends Bean{

    @Id(autoincrement = true)
    private Long id;
    private String codigo_animal;
    private String nombre;
    private String fecha_nacimiento;
    private String sexo;
    private String raza;
    private Long idLote;
    private String path_imagen;
    @ToOne(joinProperty = "idLote")
    private LoteBean LOTE;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 433853614)
    private transient InventarioBeanDao myDao;
    @Generated(hash = 20392061)
    public InventarioBean(Long id, String codigo_animal, String nombre,
            String fecha_nacimiento, String sexo, String raza, Long idLote,
            String path_imagen) {
        this.id = id;
        this.codigo_animal = codigo_animal;
        this.nombre = nombre;
        this.fecha_nacimiento = fecha_nacimiento;
        this.sexo = sexo;
        this.raza = raza;
        this.idLote = idLote;
        this.path_imagen = path_imagen;
    }
    @Generated(hash = 444504338)
    public InventarioBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCodigo_animal() {
        return this.codigo_animal;
    }
    public void setCodigo_animal(String codigo_animal) {
        this.codigo_animal = codigo_animal;
    }
    public String getNombre() {
        return this.nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getFecha_nacimiento() {
        return this.fecha_nacimiento;
    }
    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
    public String getSexo() {
        return this.sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getRaza() {
        return this.raza;
    }
    public void setRaza(String raza) {
        this.raza = raza;
    }
    public Long getIdLote() {
        return this.idLote;
    }
    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }
    @Generated(hash = 591577151)
    private transient Long LOTE__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 393505791)
    public LoteBean getLOTE() {
        Long __key = this.idLote;
        if (LOTE__resolvedKey == null || !LOTE__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LoteBeanDao targetDao = daoSession.getLoteBeanDao();
            LoteBean LOTENew = targetDao.load(__key);
            synchronized (this) {
                LOTE = LOTENew;
                LOTE__resolvedKey = __key;
            }
        }
        return LOTE;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1142304414)
    public void setLOTE(LoteBean LOTE) {
        synchronized (this) {
            this.LOTE = LOTE;
            idLote = LOTE == null ? null : LOTE.getId();
            LOTE__resolvedKey = idLote;
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
    public String getPath_imagen() {
        return this.path_imagen;
    }
    public void setPath_imagen(String path_imagen) {
        this.path_imagen = path_imagen;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 187329257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInventarioBeanDao() : null;
    }
   
}
