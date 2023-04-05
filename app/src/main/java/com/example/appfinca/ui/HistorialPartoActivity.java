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
import com.example.appfinca.adapters.AdapterPartos;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.LecheBean;
import com.example.appfinca.db.bean.PartoBean;
import com.example.appfinca.db.dao.InventarioDao;
import com.example.appfinca.db.dao.LecheDao;
import com.example.appfinca.db.dao.PartoDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class HistorialPartoActivity extends AppCompatActivity implements AdapterPartos.OnItemClickDeleteListener, AdapterPartos.OnItemClickEditListener{

    private InventarioBean inventarioBean;
    private String animalGlobal;
    private FloatingActionButton fb_agrega_parto;
    private AdapterPartos adapterPartos;
    private List<PartoBean> mDataPartos;
    private RecyclerView recyclerViewPartos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_parto);

        fb_agrega_parto = findViewById(R.id.fb_agrega_parto);
        fb_agrega_parto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogParto();
            }
        });
        inianDatos();
        initRecyclerPartos();
    }

    private void inianDatos() {
        Intent intent = this.getIntent();
        this.animalGlobal = intent.getStringExtra(Actividades.PARAM_1);
        final InventarioDao inventarioDao = new InventarioDao();
        inventarioBean = inventarioDao.getByCodigoAnimal(animalGlobal);
    }


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
        recyclerViewPartos = findViewById(R.id.recyclerView);
        mDataPartos = (List<PartoBean>) (List<?>) new PartoDao().getByCodigoAnimal(inventarioBean.getId());
        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(HistorialPartoActivity.this);
        recyclerViewPartos.setLayoutManager(manager);
        recyclerViewPartos.setHasFixedSize(true);
        adapterPartos = new AdapterPartos(mDataPartos);
        recyclerViewPartos.setAdapter(adapterPartos);
        adapterPartos.setOnItemClickDeleteListener(this);
        adapterPartos.setOnItemClickEditListener(this);
    }

    private void setDataParto(){
        mDataPartos = (List<PartoBean>) (List<?>) new PartoDao().getByCodigoAnimal(inventarioBean.getId());
        adapterPartos.setData(mDataPartos);
    }

    @Override
    public void onItemClickDelete(int position) {
        final PartoBean partoBean = mDataPartos.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(HistorialPartoActivity.this);
        builder.setTitle("Eliminar");
        builder.setMessage("Desea eliminar el registro "+ partoBean.getFecha());
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final PartoDao partoDao = new PartoDao();
                partoDao.delete(partoBean);
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
        final PartoBean partoBean = mDataPartos.get(position);
        customDialogPartoUpdate(partoBean);

    }

    private void customDialogPartoUpdate(PartoBean partoBean){
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
                    partoBean.setRegistro(numberTables);
                    update(partoBean);
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

    private void update(PartoBean partoBean) {
        final PartoDao partoDao = new PartoDao();
        partoDao.save(partoBean);
        setDataParto();
    }
}