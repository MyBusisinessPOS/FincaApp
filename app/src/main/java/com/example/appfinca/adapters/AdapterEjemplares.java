package com.example.appfinca.adapters;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinca.R;
import com.example.appfinca.db.bean.InventarioBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterEjemplares extends RecyclerView.Adapter<AdapterEjemplares.Holder> implements Filterable {

    private List<InventarioBean> mData;
    private List<InventarioBean> mDataFilter;
    private OnItemClickListener onItemClickListener;


    public AdapterEjemplares(List<InventarioBean> mData, OnItemClickListener onItemClickListener) {
        this.mData = mData;
        this.mDataFilter = mData;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterEjemplares.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventario, parent, false);
        return  new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEjemplares.Holder holder, int position) {
        holder.bind(mDataFilter.get(position),  onItemClickListener);

    }

    public void setData(List<InventarioBean> newList){
        mData = newList;
        mDataFilter = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //Contien el filtro
                String filtro = constraint.toString();

                //Si el filtro es vacion entonces la lista clientes filtro es igual a la lista original
                if (filtro.isEmpty()) {
                    mDataFilter = mData;
                } else {

                    //Creamos la lista de clientes
                    List<InventarioBean> filtroClientes = new ArrayList<>();

                    //Recorremos la lista
                    for (InventarioBean row : mData) {
                        //TODO filtro de clientes
                        if (row.getNombre().toLowerCase().contains(filtro) || row.getCodigo_animal().toLowerCase().contains(filtro)) {
                            //Agregamos el resultado a la lista
                            filtroClientes.add(row);
                        }
                    }
                    mDataFilter = filtroClientes;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFilter = (ArrayList<InventarioBean>) results.values;
                //Notifica al adaptador que cambio
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewNombre;
        TextView textViewLote;
        TextView textViewIndentificador;
        CardView card_view;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagen_ejemplar);
            textViewNombre = itemView.findViewById(R.id.tv_nombre_ejemplar);
            textViewLote = itemView.findViewById(R.id.tv_lote_ejemplar);
            textViewIndentificador = itemView.findViewById(R.id.tv_identificador_ejemplar);
            card_view = itemView.findViewById(R.id.card_view);
        }

        private void bind(InventarioBean inventarioBean, OnItemClickListener onItemClickListener){

            if (inventarioBean.getPath_imagen().compareToIgnoreCase("NO") !=0){
                String imagePath = inventarioBean.getPath_imagen();
                Uri imageUri = Uri.fromFile(new File(imagePath));
                imageView.setImageURI(imageUri);
            }

            textViewNombre.setText(""+ inventarioBean.getNombre());
            textViewLote.setText(""+ inventarioBean.getLOTE().getCategoria());
            textViewIndentificador.setText(""+ inventarioBean.getCodigo_animal());

            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }




}
