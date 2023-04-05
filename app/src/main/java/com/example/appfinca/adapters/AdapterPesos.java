package com.example.appfinca.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinca.R;
import com.example.appfinca.db.bean.PesoBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterPesos extends RecyclerView.Adapter<AdapterPesos.Holder> {

    private List<PesoBean> mData = new ArrayList<>();
    private OnItemClickDeleteListener listenerDelete;
    private OnItemClickEditListener listenerEdit;
    public void setOnItemClickDeleteListener(OnItemClickDeleteListener listener) {
        this.listenerDelete = listener;
    }

    public void setOnItemClickEditListener(OnItemClickEditListener listener) {
        this.listenerEdit = listener;
    }

    // Definición de la interfaz OnItemClickListener
    public interface OnItemClickDeleteListener {
        void onItemClickDelete(int position);
    }

    public interface OnItemClickEditListener {
        void onItemClickEdit(int position);
    }

    public AdapterPesos(List<PesoBean> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterPesos.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_peso, parent, false);
        return new AdapterPesos.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPesos.Holder holder, int position) {
        holder.bind(mData.get(position));
    }

    public void setData(List<PesoBean> newList) {
        mData = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewFecha;
        TextView textViewPeso;
        public ImageView imageViewDelete;
        public ImageView imageViewEdit;
        public Holder(@NonNull View itemView) {

            super(itemView);
            textViewFecha = itemView.findViewById(R.id.tv_peso_fecha);
            textViewPeso = itemView.findViewById(R.id.tv_peso_peso);
            imageViewDelete = itemView.findViewById(R.id.img_delete);
            imageViewEdit = itemView.findViewById(R.id.img_edit);
        }

        private void bind(PesoBean pesoBean) {

            Date fecha = pesoBean.getFecha();
            SimpleDateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String fechaFormateada = formato.format(fecha);


            textViewFecha.setText("" + fechaFormateada);
            textViewPeso.setText("" + pesoBean.getPeso() + "KG");
            ImageView imgEdit = imageViewEdit;
            ImageView imgDelete = imageViewDelete;
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listenerEdit.onItemClickEdit(position);
                    }
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listenerDelete.onItemClickDelete(position);
                    }
                }
            });

        }
    }

}