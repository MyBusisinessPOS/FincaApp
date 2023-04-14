package com.example.appfinca.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "usuarios")
public class UsuarioBean extends Bean{


    @Id(autoincrement = true)
    private Long id;
    private String email;
    private String password;
    private Boolean recordar;
    @Generated(hash = 1917164370)
    public UsuarioBean(Long id, String email, String password, Boolean recordar) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.recordar = recordar;
    }
    @Generated(hash = 1666801022)
    public UsuarioBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Boolean getRecordar() {
        return this.recordar;
    }
    public void setRecordar(Boolean recordar) {
        this.recordar = recordar;
    }


}
