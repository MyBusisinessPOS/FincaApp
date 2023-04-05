package com.example.appfinca.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.appfinca.Actividades;
import com.example.appfinca.R;
import com.example.appfinca.adapters.AdapterGestaciones;
import com.example.appfinca.adapters.AdapterLeches;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class HistorialLecheActivity extends AppCompatActivity implements AdapterLeches.OnItemClickDeleteListener, AdapterLeches.OnItemClickEditListener {


    private InventarioBean inventarioBean;
    private String animalGlobal;

    private AdapterLeches adapterLeches;
    private List<LecheBean> mDataLeche;
    private RecyclerView recyclerViewLeche;
    private FloatingActionButton fb_agrega_leche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_leche);
        fb_agrega_leche = findViewById(R.id.fb_agrega_leche);
        fb_agrega_leche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCustomLayoutDialogLeche();
            }
        });
        inianDatos();
        initRecyclerLeche();
    }

    private void inianDatos(){
        Intent intent = this.getIntent();
        this.animalGlobal = intent.getStringExtra(Actividades.PARAM_1);
        final InventarioDao inventarioDao = new InventarioDao();
        inventarioBean = inventarioDao.getByCodigoAnimal(animalGlobal);
    }

    private void getCustomLayoutDialogLeche() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_leche);
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

    private void initRecyclerLeche(){
        recyclerViewLeche = findViewById(R.id.recyclerView);
        mDataLeche = new LecheDao().getByCodigoAnimal(inventarioBean.getId());
        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(HistorialLecheActivity.this);
        recyclerViewLeche.setLayoutManager(manager);
        recyclerViewLeche.setHasFixedSize(true);
        adapterLeches = new AdapterLeches(mDataLeche);
        recyclerViewLeche.setAdapter(adapterLeches);

        adapterLeches.setOnItemClickEditListener(this);
        adapterLeches.setOnItemClickDeleteListener(this);
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

    private void setDataParto(){
        mDataLeche = new LecheDao().getByCodigoAnimal(inventarioBean.getId());
        adapterLeches.setData(mDataLeche);
    }

    @Override
    public void onItemClickDelete(int position) {
        final LecheBean lecheBean = mDataLeche.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(HistorialLecheActivity.this);
        builder.setTitle("Eliminar");
        builder.setMessage("Desea eliminar la leche "+ lecheBean.getCantidad());
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final LecheDao pesoDao = new LecheDao();
                pesoDao.delete(lecheBean);
                setDataParto();
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

        final LecheBean lecheBean = mDataLeche.get(position);
        getCustomLayoutDialogLecheUpdate(lecheBean);
    }


    private void getCustomLayoutDialogLecheUpdate(LecheBean lecheBean) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_leche);
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
                    lecheBean.setCantidad(numberTables);
                    update(lecheBean);
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

    private void update(LecheBean lecheBean) {
        final LecheDao lecheDao = new LecheDao();
        lecheDao.save(lecheBean);
        setDataParto();
    }
}