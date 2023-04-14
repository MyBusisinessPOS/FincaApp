package com.example.appfinca.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.appfinca.Actividades;
import com.example.appfinca.databinding.ActivityLoginBinding;
import com.example.appfinca.db.bean.UsuarioBean;
import com.example.appfinca.db.dao.UsuarioDao;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity {


    private EditText edit_login_user, edit_login_password;
    private Switch switch_remember;
    private MaterialButton button_login_access;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicianControles();
    }

    private void inicianControles() {

        edit_login_user = binding.editLoginUser;
        edit_login_password = binding.editLoginPassword;
        switch_remember = binding.switchRemember;
        button_login_access = binding.buttonLoginAccess;

        button_login_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaDatos()){
                    final UsuarioDao usuarioDao = new UsuarioDao();
                    final UsuarioBean usuarioBean = usuarioDao.login(edit_login_user.getText().toString(), edit_login_password.getText().toString());
                    if (usuarioBean != null){
                        if (switch_remember.isChecked()){
                            usuarioBean.setRecordar(true);
                            usuarioDao.save(usuarioBean);
                        }
                        Actividades.getSingleton(LoginActivity.this, MainActivity.class).muestraActividad();
                    }else {
                        Toast.makeText(LoginActivity.this, "Credenciales invalidas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private boolean  validaDatos(){
        return !edit_login_user.getText().toString().isEmpty() || !edit_login_password.getText().toString().isEmpty();
    }

}