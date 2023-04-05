package com.example.appfinca.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfinca.R;
import com.example.appfinca.db.bean.LecheBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterLeches extends RecyclerView.Adapter<AdapterLeches.Holder> {

    private List<LecheBean> mData;

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

    public AdapterLeches(List<LecheBean> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterLeches.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leche, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLeches.Holder holder, int position) {
        holder.bind(mData.get(position));
    }

    public void setData(List<LecheBean> newData){
        mData = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewFecha;
        TextView textViewCantidad;

         ImageView imageViewDelete;
         ImageView imageViewEdit;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.tv_leche_fecha);
            textViewCantidad = itemView.findViewById(R.id.tv_leche_cantidad);
            imageViewDelete = itemView.findViewById(R.id.img_delete);
            imageViewEdit = itemView.findViewById(R.id.img_edit);
        }

        private void bind(final LecheBean lecheBean){
            Date fecha = lecheBean.getFecha();
            SimpleDateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String fechaFormateada = formato.format(fecha);
            textViewFecha.setText(fechaFormateada);
            textViewCantidad.setText("" + lecheBean.getCantidad());

            // Obtener referencias a los ImageView aquí
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
