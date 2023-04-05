package com.example.appfinca.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;

import com.example.appfinca.Actividades;
import com.example.appfinca.R;
import com.example.appfinca.adapters.AdapterEjemplares;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.LoteBean;
import com.example.appfinca.db.dao.InventarioDao;
import com.example.appfinca.db.dao.LoteDao;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAgregarEjemplar;

    private AdapterEjemplares adapterEjemplares;
    private List<InventarioBean> mData;
    private RecyclerView recyclerView;

    private SearchView searchView_ejemplares;
    private AutoCompleteTextView mAutoCompleteTextView;
    private List<LoteBean> listadoLotes;
    LoteBean selectedLote = null;

    MaterialButton btnTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarControles();
    }

    private void iniciarControles(){

        btnTodos = findViewById(R.id.btnTodos);
        btnTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLote = null;
                mAutoCompleteTextView.setText("");
                setData();
            }
        });
        searchView_ejemplares = findViewById(R.id.searchView_ejemplares);
        searchView_ejemplares.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchView_ejemplares.setQuery("", false);
                }

            }
        });

        searchView_ejemplares.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterEjemplares.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterEjemplares.getFilter().filter(newText);
                return false;
            }
        });

         btnAgregarEjemplar = findViewById(R.id.btnAgregarEjemplar);
        btnAgregarEjemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put(Actividades.PARAM_1, "CREATE");
                Actividades.getSingleton(MainActivity.this, RegistroEspecieActivity.class).muestraActividad(parametros);
            }
        });

        recyclerView  = findViewById(R.id.rv_list_ejemplares);

        if (selectedLote == null){
            mData = (List<InventarioBean>) (List<?>) new InventarioDao().list();
        }



        /*** ----- Manejador ------ ****/
        final LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapterEjemplares = new AdapterEjemplares(mData, new AdapterEjemplares.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                InventarioBean inventarioBean = mData.get(position);
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put(Actividades.PARAM_1, inventarioBean.getCodigo_animal());
                Actividades.getSingleton(MainActivity.this, MenuOpcionesActivity.class).muestraActividad(parametros);
                return;
            }
        });
        recyclerView.setAdapter(adapterEjemplares);


        cargaLotes();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setData();
    }

    private void setData(){
        if (selectedLote == null){
            mData = (List<InventarioBean>) (List<?>) new InventarioDao().list();
        }else {
            mData = (List<InventarioBean>) (List<?>) new InventarioDao().getAllLote(selectedLote.getId());
        }
        adapterEjemplares.setData(mData);
    }


    private void cargaLotes() {
        listadoLotes = (List<LoteBean>) (List<?>) new LoteDao().list();
        loadAutomcompleteLotes();
    }

    private void loadAutomcompleteLotes() {
        ArrayAdapter<LoteBean> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listadoLotes);
        mAutoCompleteTextView = findViewById(R.id.dropdownLote);
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLote = (LoteBean) parent.getItemAtPosition(position);
                setData();
            }
        });
        mAutoCompleteTextView.setAdapter(adapter);
    }
}