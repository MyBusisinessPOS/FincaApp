package com.example.appfinca.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinca.R;
import com.example.appfinca.db.bean.GestacionBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterGestaciones extends RecyclerView.Adapter<AdapterGestaciones.Holder> {

    private List<GestacionBean>mData;

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


    public AdapterGestaciones(List<GestacionBean> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterGestaciones.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gestacion, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGestaciones.Holder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewFecha;
        TextView textViewEstado;
        ImageView imageViewDelete;
        ImageView imageViewEdit;
        public Holder(@NonNull View itemView) {
            super(itemView);

            textViewFecha = itemView.findViewById(R.id.tv_fecha_gestacion);
            textViewEstado = itemView.findViewById(R.id.tv_estado_gestacion);
            imageViewDelete = itemView.findViewById(R.id.img_delete);
            imageViewEdit = itemView.findViewById(R.id.img_edit);
        }

        private void bind(GestacionBean gestacionBean){
            Date fecha = gestacionBean.getFecha();
            SimpleDateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String fechaFormateada = formato.format(fecha);
            textViewFecha.setText("" + fechaFormateada);
            textViewEstado.setText("" + gestacionBean.getEstado());

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

    public void setData(List<GestacionBean> newData){
        mData = newData;
        notifyDataSetChanged();
    }
}
