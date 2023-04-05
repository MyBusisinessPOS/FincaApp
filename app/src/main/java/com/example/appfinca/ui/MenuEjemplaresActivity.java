package com.example.appfinca.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.appfinca.Actividades;
import com.example.appfinca.R;
import com.example.appfinca.adapters.AdapterGestaciones;
import com.example.appfinca.adapters.AdapterLeches;
import com.example.appfinca.adapters.AdapterPartos;
import com.example.appfinca.adapters.AdapterPesos;
import com.example.appfinca.db.bean.GestacionBean;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuEjemplaresActivity extends AppCompatActivity {


    private String animalGlobal;
    private MaterialButton btnPeso, btnParto, btnGestacion, btnLecha;
    private LinearLayout ll_opciones;
    private InventarioBean inventarioBean;


    private AdapterPesos adapterPesos;
    private List<PesoBean> mDataPesos;
    private RecyclerView recyclerViewPesos;


    private AdapterPartos adapterPartos;
    private List<PartoBean> mDataPartos;
    private RecyclerView recyclerViewPartos;

    //Todo Gestaciones
    private AdapterGestaciones adapterGestaciones;
    private List<GestacionBean> mDataGestacion;
    private RecyclerView recyclerViewGestacion;
    private String gestacionSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ejemplares);
        iniciaDatos();
        initControls();

    }

    private void iniciaDatos() {

        ll_opciones = findViewById(R.id.ll_opciones);
        Intent intent = this.getIntent();
        this.animalGlobal = intent.getStringExtra(Actividades.PARAM_1);


        final InventarioDao inventarioDao = new InventarioDao();
        inventarioBean = inventarioDao.getByCodigoAnimal(animalGlobal);

        if (inventarioBean == null) {
            return;
        }

        if (inventarioBean.getSexo().compareToIgnoreCase("Hembra") == 0) {
            ll_opciones.setVisibility(View.VISIBLE);
        } else {
            ll_opciones.setVisibility(View.GONE);
        }

    }

    private void initControls() {


        btnPeso = findViewById(R.id.btnPeso);
        btnPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCustomLayoutDialog();
            }
        });

        btnParto = findViewById(R.id.btnParto);
        btnParto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogParto();
            }
        });

        btnGestacion = findViewById(R.id.btnGestacion);
        btnGestacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogGestacion();
            }
        });

        btnLecha = findViewById(R.id.btnLecha);
        btnLecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mDataPesos = new ArrayList<>();
        mDataGestacion = new ArrayList<>();
        mDataPartos = new ArrayList<>();
        initRecyclerPesos();
        initRecyclerPartos();
        initRecyclerGestacion();
    }


    //TODO PESO
    private void getCustomLayoutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_peso);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            EditText amountEditText = dialog.findViewById(R.id.edit_amount);
            MaterialButton positiveButton = dialog.findViewById(R.id.okButton);
            MaterialButton negativeButton = dialog.findViewById(R.id.cancelButton);

            positiveButton.setEnabled(false);
            // Colocar el foco en el EditText y abrir el teclado
            amountEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            amountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String input = s.toString();

                    if (input.isEmpty()) {
                        amountEditText.setError("Ingrese un valor numérico mayor a cero");
                        positiveButton.setEnabled(false);
                    } else {
                        try {
                            Double enteredValue = Double.parseDouble(input);
                            if (enteredValue <= 0) {
                                amountEditText.setError("Ingrese un valor numérico mayor a cero");
                                positiveButton.setEnabled(false);
                            } else {
                                amountEditText.setError(null);
                                positiveButton.setEnabled(true);
                            }
                        } catch (NumberFormatException e) {
                            amountEditText.setError("Ingrese un valor numérico válido");
                            positiveButton.setEnabled(false);
                        }
                    }
                }
            });

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double numberTables = Double.parseDouble(amountEditText.getText().toString());
                    imm.hideSoftInputFromWindow(amountEditText.getWindowToken(), 0);
                    agregarPeso(numberTables);
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
    private void agregarPeso(Double peso){
        final PesoDao pesoDao =new PesoDao();
        final PesoBean pesoBean = new PesoBean();
        pesoBean.setFecha(new Date());
        pesoBean.setINVENTARIO(inventarioBean);
        pesoBean.setPeso(peso);
        pesoDao.save(pesoBean);
        setDataPesos();
    }

    private void initRecyclerPesos(){
        recyclerViewPesos  = findViewById(R.id.rv_pesos);
        mDataPesos = new ArrayList<>();
        mDataPesos = (List<PesoBean>) (List<?>) new PesoDao().getByCodigoAnimal(inventarioBean.getId());
        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(MenuEjemplaresActivity.this);
        recyclerViewPesos.setLayoutManager(manager);
        recyclerViewPesos.setHasFixedSize(true);
        adapterPesos = new AdapterPesos(mDataPesos);
        recyclerViewPesos.setAdapter(adapterPesos);
    }

    private void setDataPesos(){
        mDataPesos = (List<PesoBean>) (List<?>) new PesoDao().getByCodigoAnimal(inventarioBean.getId());
        adapterPesos.setData(mDataPesos);
    }


    //TODO LECHE
    private void getCustomLayoutDialogLeche() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_peso);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            EditText amountEditText = dialog.findViewById(R.id.edit_amount);
            MaterialButton positiveButton = dialog.findViewById(R.id.okButton);
            MaterialButton negativeButton = dialog.findViewById(R.id.cancelButton);

            positiveButton.setEnabled(false);
            // Colocar el foco en el EditText y abrir el teclado
            amountEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            amountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String input = s.toString();

                    if (input.isEmpty()) {
                        amountEditText.setError("Ingrese un valor numérico mayor a cero");
                        positiveButton.setEnabled(false);
                    } else {
                        try {
                            Double enteredValue = Double.parseDouble(input);
                            if (enteredValue <= 0) {
                                amountEditText.setError("Ingrese un valor numérico mayor a cero");
                                positiveButton.setEnabled(false);
                            } else {
                                amountEditText.setError(null);
                                positiveButton.setEnabled(true);
                            }
                        } catch (NumberFormatException e) {
                            amountEditText.setError("Ingrese un valor numérico válido");
                            positiveButton.setEnabled(false);
                        }
                    }
                }
            });

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double numberTables = Double.parseDouble(amountEditText.getText().toString());
                    imm.hideSoftInputFromWindow(amountEditText.getWindowToken(), 0);
                    agregarLeche(numberTables);
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
    private void agregarLeche(Double peso){
        final LecheDao lecheDao =new LecheDao();
        final LecheBean lecheBean = new LecheBean();
        lecheBean.setFecha(new Date());
        lecheBean.setINVENTARIO(inventarioBean);
        lecheBean.setCantidad(peso);
        lecheDao.save(lecheBean);
        setDataParto();
    }

    //TODO Partos
    private void customDialogParto(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_parto);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            EditText amountEditText = dialog.findViewById(R.id.edit_notes);
            MaterialButton positiveButton = dialog.findViewById(R.id.okButton);
            MaterialButton negativeButton = dialog.findViewById(R.id.cancelButton);

            positiveButton.setEnabled(false);
            // Colocar el foco en el EditText y abrir el teclado
            amountEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            amountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String input = s.toString();

                    if (input.isEmpty()) {
                        amountEditText.setError("Ingrese las observaciones");
                        positiveButton.setEnabled(false);
                    }else {
                        amountEditText.setError(null);
                        positiveButton.setEnabled(true);
                    }
                }
            });

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String numberTables = amountEditText.getText().toString();
                    imm.hideSoftInputFromWindow(amountEditText.getWindowToken(), 0);
                    agregarParto(numberTables);
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

    private void agregarParto(String notas){
        final PartoDao partoDao =new PartoDao();
        final PartoBean partoBean = new PartoBean();
        partoBean.setFecha(new Date());
        partoBean.setINVENTARIO(inventarioBean);
        partoBean.setRegistro(notas);
        partoDao.save(partoBean);
        setDataParto();
    }

    private void initRecyclerPartos(){
        recyclerViewPartos = findViewById(R.id.rv_partos);
        mDataPartos = (List<PartoBean>) (List<?>) new PartoDao().getByCodigoAnimal(inventarioBean.getId());
        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(MenuEjemplaresActivity.this);
        recyclerViewPartos.setLayoutManager(manager);
        recyclerViewPartos.setHasFixedSize(true);
        adapterPartos = new AdapterPartos(mDataPartos);
        recyclerViewPartos.setAdapter(adapterPartos);
    }

    private void setDataParto(){
        mDataPartos = (List<PartoBean>) (List<?>) new PartoDao().getByCodigoAnimal(inventarioBean.getId());
        adapterPartos.setData(mDataPartos);
    }

    //TODO Gestacion
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
        recyclerViewGestacion = findViewById(R.id.rv_gestacion);
        mDataGestacion = (List<GestacionBean>) (List<?>) new GestacionDao().getByCodigoAnimal(inventarioBean.getId());
        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(MenuEjemplaresActivity.this);
        recyclerViewGestacion.setLayoutManager(manager);
        recyclerViewGestacion.setHasFixedSize(true);
        adapterGestaciones = new AdapterGestaciones(mDataGestacion);
        recyclerViewGestacion.setAdapter(adapterGestaciones);
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


}