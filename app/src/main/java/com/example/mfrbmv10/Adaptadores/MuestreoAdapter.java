package com.example.mfrbmv10.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

import java.util.List;

public class MuestreoAdapter extends RecyclerView.Adapter<MuestreoAdapter.MyViewHolder> {

    private Context mContext;
    private List<Muestreo> listaMuestreos;
    private OnItemClickListener listener;

    public MuestreoAdapter(Context context, List<Muestreo> uploads) {
        mContext = context;
        listaMuestreos = uploads;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_muestreo, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Muestreo muestreo = listaMuestreos.get(position);
        holder.tv_nombre_mtr.setText(muestreo.getNombre_mtr());
        holder.tv_localizacion_mtr.setText(muestreo.getUbicacion_mtr());
        //holder.iv_imagen_mtr.setImageResource(R.drawable.flores1);
        /*Glide.with(mContext)
                .load(muestreo.getImagen_mtr())
                .apply(new RequestOptions().override(80, 80))
                .into(holder.iv_imagen_mtr);*/
        verificarImagen(muestreo.getImagen_mtr(), holder);
    }

    @Override
    public int getItemCount() {
        return listaMuestreos.size();
    }

    public void verificarImagen(String imagen, MyViewHolder holder){
        if(imagen == ""){
            holder.iv_imagen_mtr.setImageResource(R.drawable.flores1);
        }else{
            Glide.with(mContext)
                    .load(imagen)
                    .apply(new RequestOptions().override(80, 80))
                    .into(holder.iv_imagen_mtr);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nombre_mtr, tv_localizacion_mtr;
        ImageView iv_imagen_mtr;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_nombre_mtr = itemView.findViewById(R.id.tv_nombre_mtr);
            tv_localizacion_mtr = itemView.findViewById(R.id.tv_localizacion_mtr);
            iv_imagen_mtr = itemView.findViewById(R.id.iv_imagen_mtr);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(listaMuestreos.get(posicion), posicion);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemLongClick(listaMuestreos.get(posicion), posicion);
                    }
                    return true;
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Muestreo documentSnapshot, int posicion);

        void onItemLongClick(Muestreo documentReference, int posicion);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}

