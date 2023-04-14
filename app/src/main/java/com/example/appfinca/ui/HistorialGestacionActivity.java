package com.example.appfinca.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.example.appfinca.Actividades;
import com.example.appfinca.R;
import com.example.appfinca.adapters.AdapterGestaciones;
import com.example.appfinca.db.bean.GestacionBean;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.LecheBean;
import com.example.appfinca.db.dao.GestacionDao;
import com.example.appfinca.db.dao.InventarioDao;
import com.example.appfinca.db.dao.LecheDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialGestacionActivity extends AppCompatActivity implements AdapterGestaciones.OnItemClickEditListener, AdapterGestaciones.OnItemClickDeleteListener{
    private InventarioBean inventarioBean;
    private String animalGlobal;

    private AdapterGestaciones adapterGestaciones;
    private List<GestacionBean> mDataGestacion;
    private RecyclerView recyclerViewGestacion;
    private String gestacionSeleccionada;
    private FloatingActionButton fb_agrega_gestacion;

    private ImageView img_close_documents_gestacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_gestacion);

        fb_agrega_gestacion = findViewById(R.id.fb_agrega_gestacion);
        fb_agrega_gestacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogGestacion();
            }
        });

        inianDatos();
        initRecyclerGestacion();

        img_close_documents_gestacion = findViewById(R.id.img_close_documents_gestacion);
        img_close_documents_gestacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void inianDatos(){
        Intent intent = this.getIntent();
        this.animalGlobal = intent.getStringExtra(Actividades.PARAM_1);
        final InventarioDao inventarioDao = new InventarioDao();
        inventarioBean = inventarioDao.getByCodigoAnimal(animalGlobal);
    }

    private void customDialogGestacion(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_gestacion);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        String[] array = this.getArrayString(R.array.estados_gestacion);

        //Obtiene la lista de strings
        List<String> arrayList = this.convertArrayString2ListString(array);

        if (dialog.getWindow() != null) {

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            AutoCompleteTextView mAutoCompleteTextView = dialog.findViewById(R.id.dropdownGestacion);

            MaterialButton positiveButton = dialog.findViewById(R.id.okButton);
            MaterialButton negativeButton = dialog.findViewById(R.id.cancelButton);

            positiveButton.setEnabled(false);
            // Colocar el foco en el EditText y abrir el teclado
            mAutoCompleteTextView.requestFocus();



            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
            mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gestacionSeleccionada = (String) parent.getItemAtPosition(position);
                    positiveButton.setEnabled(true);

                }
            });
            mAutoCompleteTextView.setAdapter(adapter);


            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agregarGestacion(gestacionSeleccionada);
                    dialog.cancel();
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    private void agregarGestacion(String notas){
        final GestacionDao gestacionDao =new GestacionDao();
        final GestacionBean gestacionBean = new GestacionBean();
        gestacionBean.setFecha(new Date());
        gestacionBean.setINVENTARIO(inventarioBean);
        gestacionBean.setEstado(notas);
        gestacionDao.save(gestacionBean);
        setDataGestacion();
    }

    private void initRecyclerGestacion(){
        recyclerViewGestacion = findViewById(R.id.recyclerView);
        mDataGestacion = (List<GestacionBean>) (List<?>) new GestacionDao().getByCodigoAnimal(inventarioBean.getId());
        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(HistorialGestacionActivity.this);
        recyclerViewGestacion.setLayoutManager(manager);
        recyclerViewGestacion.setHasFixedSize(true);
        adapterGestaciones = new AdapterGestaciones(mDataGestacion);
        recyclerViewGestacion.setAdapter(adapterGestaciones);
        adapterGestaciones.setOnItemClickDeleteListener(this);
        adapterGestaciones.setOnItemClickEditListener(this);
    }

    private void setDataGestacion(){
        mDataGestacion = (List<GestacionBean>) (List<?>) new GestacionDao().getByCodigoAnimal(inventarioBean.getId());
        adapterGestaciones.setData(mDataGestacion);
    }



    final protected String[] getArrayString(final int id){
        return getResources().getStringArray(id);
    }

    final protected List<String> convertArrayString2ListString(final String[] arrayString){

        //Contiene la lista
        List<String> lista = new ArrayList<>();

        //Recorre el array de strings
        for(String string: arrayString)
            lista.add(string);

        //Devuelve el resultado
        return lista;
    }

    @Override
    public void onItemClickDelete(int position) {
        final GestacionBean gestacionBean = mDataGestacion.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(HistorialGestacionActivity.this);
        builder.setTitle("Eliminar");
        builder.setMessage("Desea eliminar la gestacion "+ gestacionBean.getFecha());
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final GestacionDao gestacionDao = new GestacionDao();
                gestacionDao.delete(gestacionBean);
                setDataGestacion();
                dialog.dismiss();
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

    @Override
    public void onItemClickEdit(int position) {
        final GestacionBean gestacionBean = mDataGestacion.get(position);
        customDialogGestacionUpdate(gestacionBean);
    }


    private void customDialogGestacionUpdate(GestacionBean gestacionBean){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_gestacion);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        String[] array = this.getArrayString(R.array.estados_gestacion);

        //Obtiene la lista de strings
        List<String> arrayList = this.convertArrayString2ListString(array);

        if (dialog.getWindow() != null) {

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            AutoCompleteTextView mAutoCompleteTextView = dialog.findViewById(R.id.dropdownGestacion);

            MaterialButton positiveButton = dialog.findViewById(R.id.okButton);
            MaterialButton negativeButton = dialog.findViewById(R.id.cancelButton);

            positiveButton.setEnabled(false);
            // Colocar el foco en el EditText y abrir el teclado
            mAutoCompleteTextView.requestFocus();



            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
            mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gestacionSeleccionada = (String) parent.getItemAtPosition(position);
                    positiveButton.setEnabled(true);

                }
            });
            mAutoCompleteTextView.setAdapter(adapter);


            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gestacionBean.setEstado(gestacionSeleccionada);
                    update(gestacionBean);
                    dialog.cancel();
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    private void update(GestacionBean gestacionBean){
        final GestacionDao gestacionDao = new GestacionDao();
        gestacionDao.save(gestacionBean);
        setDataGestacion();
    }

}