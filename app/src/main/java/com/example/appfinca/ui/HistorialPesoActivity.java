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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appfinca.Actividades;
import com.example.appfinca.R;
import com.example.appfinca.adapters.AdapterPesos;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.PesoBean;
import com.example.appfinca.db.dao.InventarioDao;
import com.example.appfinca.db.dao.PesoDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialPesoActivity extends AppCompatActivity implements AdapterPesos.OnItemClickDeleteListener, AdapterPesos.OnItemClickEditListener {

    private InventarioBean inventarioBean;

    private AdapterPesos adapterPesos;
    private List<PesoBean> mDataPesos;
    private RecyclerView recyclerViewPesos;
    private String animalGlobal;

    private FloatingActionButton fb_agrega_peso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_peso);

        fb_agrega_peso = findViewById(R.id.fb_agrega_peso);
        fb_agrega_peso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCustomLayoutDialog();
            }
        });
        inianDatos();
        initRecyclerPesos();
    }

    private void inianDatos() {
        Intent intent = this.getIntent();
        this.animalGlobal = intent.getStringExtra(Actividades.PARAM_1);
        final InventarioDao inventarioDao = new InventarioDao();
        inventarioBean = inventarioDao.getByCodigoAnimal(animalGlobal);
    }

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

    private void agregarPeso(Double peso) {
        final PesoDao pesoDao = new PesoDao();
        final PesoBean pesoBean = new PesoBean();
        pesoBean.setFecha(new Date());
        pesoBean.setINVENTARIO(inventarioBean);
        pesoBean.setPeso(peso);
        pesoDao.save(pesoBean);
        setDataPesos();
    }

    private void initRecyclerPesos() {
        recyclerViewPesos = findViewById(R.id.recyclerView);
        mDataPesos = new ArrayList<>();
        mDataPesos = (List<PesoBean>) (List<?>) new PesoDao().getByCodigoAnimal(inventarioBean.getId());
        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(HistorialPesoActivity.this);
        recyclerViewPesos.setLayoutManager(manager);
        recyclerViewPesos.setHasFixedSize(true);
        adapterPesos = new AdapterPesos(mDataPesos);
        recyclerViewPesos.setAdapter(adapterPesos);
        adapterPesos.setOnItemClickEditListener(this);
        adapterPesos.setOnItemClickDeleteListener(this);
    }

    private void setDataPesos() {
        mDataPesos = (List<PesoBean>) (List<?>) new PesoDao().getByCodigoAnimal(inventarioBean.getId());
        adapterPesos.setData(mDataPesos);
    }

    @Override
    public void onItemClickDelete(int position) {
        final PesoBean pesoBean = mDataPesos.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(HistorialPesoActivity.this);
        builder.setTitle("Eliminar");
        builder.setMessage("Desea eliminar el peso "+ pesoBean.getPeso());
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final PesoDao pesoDao = new PesoDao();
                pesoDao.delete(pesoBean);
                setDataPesos();
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
        final PesoBean pesoBean = mDataPesos.get(position);
        getCustomLayoutDialogUpdate(pesoBean);
    }

    private void update(final PesoBean pesoBean){
        final PesoDao pesoDao = new PesoDao();
        pesoDao.save(pesoBean);
        setDataPesos();
    }


    private void getCustomLayoutDialogUpdate(PesoBean pesoBean) {

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
                    pesoBean.setPeso(numberTables);
                    update(pesoBean);
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



}

