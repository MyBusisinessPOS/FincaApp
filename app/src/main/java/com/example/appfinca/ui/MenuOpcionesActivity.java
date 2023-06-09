package com.example.appfinca.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appfinca.Actividades;
import com.example.appfinca.R;
import com.example.appfinca.db.bean.GestacionBean;
import com.example.appfinca.db.bean.GestacionBeanDao;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.LecheBean;
import com.example.appfinca.db.bean.PartoBean;
import com.example.appfinca.db.bean.PesoBean;
import com.example.appfinca.db.dao.GestacionDao;
import com.example.appfinca.db.dao.InventarioDao;
import com.example.appfinca.db.dao.LecheDao;
import com.example.appfinca.db.dao.PartoDao;
import com.example.appfinca.db.dao.PesoDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.greendao.annotation.ToOne;

import java.util.HashMap;

public class MenuOpcionesActivity extends AppCompatActivity {
    private InventarioBean inventarioBean;
    private String animalGlobal;
    private ImageView  imageViewPeso ,  imageViewParto, imageViewGestion, imageViewLeche, img_close_documents;
    private TextView editar_ejemplar;

    private TextView tv_codigo, tv_nombre, tv_sexo, tv_lote,tv_raza,tv_fecha_nacimiento;

    private FloatingActionButton fbEliminar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_opciones);
        iniciaControles();
        iniciaDatos();
    }

    private void iniciaControles() {


        tv_codigo = findViewById(R.id.tv_codigo);
        tv_nombre  = findViewById(R.id.tv_nombre);
        tv_sexo = findViewById(R.id.tv_sexo);
        tv_lote = findViewById(R.id.tv_lote);
        tv_raza = findViewById(R.id.tv_raza);
        tv_fecha_nacimiento = findViewById(R.id.tv_fecha_nacimiento);

        editar_ejemplar = findViewById(R.id.editar_ejemplar);
        imageViewPeso = findViewById(R.id.image_view_peso);
        imageViewParto = findViewById(R.id.image_view_parto);
        imageViewGestion = findViewById(R.id.image_view_gestacion);
        imageViewLeche = findViewById(R.id.image_view_lecche);

        img_close_documents = findViewById(R.id.img_close_documents);
        img_close_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fbEliminar = findViewById(R.id.fb_detele);

        fbEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuOpcionesActivity.this);
                builder.setTitle("Eliminar");
                builder.setMessage("Desea eliminar este ejemplar ");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final InventarioDao inventarioDao = new InventarioDao();
                        inventarioBean = inventarioDao.getByCodigoAnimal(animalGlobal);

                        if (inventarioBean != null){
                            inventarioDao.delete(inventarioBean);

                            if (inventarioBean.getSexo().compareToIgnoreCase("Hembra") == 0){
                                final GestacionDao gestacionBeanDao = new GestacionDao();
                                final GestacionBean gestacionBean = gestacionBeanDao.animal(inventarioBean.getId());
                                if (gestacionBean != null){
                                    gestacionBeanDao.delete(gestacionBean);
                                }


                                final LecheDao lecheDao = new LecheDao();
                                final LecheBean lecheBean = lecheDao.animal(inventarioBean.getId());
                                if ( lecheBean != null){
                                    lecheDao.delete(lecheBean);
                                }


                                final PartoDao partoDao = new PartoDao();
                                final PartoBean partoBean = partoDao.animal(inventarioBean.getId());
                                if ( partoBean != null){
                                    partoDao.delete(partoBean);
                                }

                            }


                            final PesoDao pesoDao = new PesoDao();
                            final PesoBean pesoBean = pesoDao.animal(inventarioBean.getId());
                            if(pesoBean != null){
                                pesoDao.delete(pesoBean);
                            }



                        }

                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void iniciaDatos() {

        Intent intent = this.getIntent();
        this.animalGlobal = intent.getStringExtra(Actividades.PARAM_1);

        final InventarioDao inventarioDao = new InventarioDao();
        inventarioBean = inventarioDao.getByCodigoAnimal(animalGlobal);

        if (inventarioBean == null) {
            return;
        }


        tv_codigo.setText("Código: "+ inventarioBean.getCodigo_animal());
        tv_nombre.setText("Nombre: "+ inventarioBean.getNombre());
        tv_sexo.setText(  "Sexo:   "+ inventarioBean.getSexo());
        tv_lote.setText(  "Lote:   "+ inventarioBean.getLOTE());
        tv_raza.setText(  "Raza:   "+ inventarioBean.getRaza());
        tv_fecha_nacimiento.setText("Fecha nacimiento: "+ inventarioBean.getFecha_nacimiento());

        Glide.with(MenuOpcionesActivity.this)
                .load(R.drawable.icon_peso)
                .into(imageViewPeso);

        Glide.with(MenuOpcionesActivity.this)
                .load(R.drawable.icon_parto)
                .into(imageViewParto);

        Glide.with(MenuOpcionesActivity.this)
                .load(R.drawable.gestacion)
                .into(imageViewGestion);
        Glide.with(MenuOpcionesActivity.this)
                .load(R.drawable.icon_leche)
                .into(imageViewLeche);


        if (inventarioBean.getSexo().compareToIgnoreCase("Hembra") == 0) {

           imageViewPeso.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   HashMap<String, String> parametros = new HashMap<>();
                   parametros.put(Actividades.PARAM_1, inventarioBean.getCodigo_animal());
                   Actividades.getSingleton(MenuOpcionesActivity.this, HistorialPesoActivity.class).muestraActividad(parametros);
               }
           });

            imageViewParto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> parametros = new HashMap<>();
                    parametros.put(Actividades.PARAM_1, inventarioBean.getCodigo_animal());
                    Actividades.getSingleton(MenuOpcionesActivity.this, HistorialPartoActivity.class).muestraActividad(parametros);
                }
            });

            imageViewGestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> parametros = new HashMap<>();
                    parametros.put(Actividades.PARAM_1, inventarioBean.getCodigo_animal());
                    Actividades.getSingleton(MenuOpcionesActivity.this, HistorialGestacionActivity.class).muestraActividad(parametros);
                }
            });

            imageViewLeche.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> parametros = new HashMap<>();
                    parametros.put(Actividades.PARAM_1, inventarioBean.getCodigo_animal());
                    Actividades.getSingleton(MenuOpcionesActivity.this, HistorialLecheActivity.class).muestraActividad(parametros);

                }
            });

        } else {
            imageViewPeso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HashMap<String, String> parametros = new HashMap<>();
                    parametros.put(Actividades.PARAM_1, inventarioBean.getCodigo_animal());
                    Actividades.getSingleton(MenuOpcionesActivity.this, HistorialPesoActivity.class).muestraActividad(parametros);
                }
            });

            imageViewParto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuOpcionesActivity.this, "Esta opcion no esta disponibles para Manchos", Toast.LENGTH_SHORT).show();
                }
            });

            imageViewGestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuOpcionesActivity.this, "Esta opcion no esta disponibles para Manchos", Toast.LENGTH_SHORT).show();
                }
            });

            imageViewLeche.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuOpcionesActivity.this, "Esta opcion no esta disponibles para Manchos", Toast.LENGTH_SHORT).show();

                }
            });
        }

        editar_ejemplar.setText("Editar");
        editar_ejemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put(Actividades.PARAM_1, inventarioBean.getCodigo_animal());
                Actividades.getSingleton(MenuOpcionesActivity.this, RegistroEspecieActivity.class).muestraActividad(parametros);
            }
        });

    }
}