package com.example.appfinca.db.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "lotes")
public class LoteBean extends Bean{

    @Id(autoincrement = true)
    private Long id;
    private String categoria;
    @Generated(hash = 324902080)
    public LoteBean(Long id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }
    @Generated(hash = 1827620912)
    public LoteBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCategoria() {
        return this.categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    @Override
    public String toString() {
        return categoria;
    }
}
