package com.example.appfinca.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinca.R;
import com.example.appfinca.db.bean.PartoBean;
import com.example.appfinca.db.bean.PesoBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterPartos extends RecyclerView.Adapter<AdapterPartos.Holder> {

    private List<PartoBean> mData;

    private OnItemClickDeleteListener listenerDelete;
    private OnItemClickEditListener listenerEdit;
    public void setOnItemClickDeleteListener(OnItemClickDeleteListener listener) {
        this.listenerDelete = listener;
    }
    public void setOnItemClickEditListener(OnItemClickEditListener listener) {
        this.listenerEdit = listener;
    }
    public interface OnItemClickDeleteListener {
        void onItemClickDelete(int position);
    }
    public interface OnItemClickEditListener {
        void onItemClickEdit(int position);
    }

    public AdapterPartos(List<PartoBean> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterPartos.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partos, parent, false);
        return new AdapterPartos.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPartos.Holder holder, int position) {
        holder.bind(mData.get(position));

    }

    public void setData(List<PartoBean> newList) {
        mData = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textViewFecha;
        TextView textViewRegistro;
        ImageView imageViewDelete;
        ImageView imageViewEdit;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.tv_fecha_parto);
            textViewRegistro = itemView.findViewById(R.id.tv_registro_parto);
            imageViewDelete = itemView.findViewById(R.id.img_delete);
            imageViewEdit = itemView.findViewById(R.id.img_edit);
        }

        private void bind(PartoBean pesoBean) {

            Date fecha = pesoBean.getFecha();
            SimpleDateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String fechaFormateada = formato.format(fecha);

            textViewFecha.setText("" + fechaFormateada);
            textViewRegistro.setText("" + pesoBean.getRegistro());

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
