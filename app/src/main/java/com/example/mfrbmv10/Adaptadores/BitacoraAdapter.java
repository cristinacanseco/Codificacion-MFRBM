package com.example.mfrbmv10.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfrbmv10.Fragments.Bitacoras.BitacoraFragment;
import com.example.mfrbmv10.Modelos.Bitacora;
import com.example.mfrbmv10.R;

import java.util.List;

public class BitacoraAdapter extends RecyclerView.Adapter<BitacoraAdapter.MyViewHolder> {

    private Context mContext;
    private List<Bitacora> listaBitacoras;
    private OnItemClickListener listener;

    public BitacoraAdapter(Context context, List<Bitacora> uploads) {
        mContext = context;
        listaBitacoras = uploads;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_bitacora, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bitacora bitacora = listaBitacoras.get(position);
        holder.tv_nombre_btc.setText(bitacora.getNombre_btc());
        holder.tv_descripcion_btc.setText(bitacora.getUbicacion_btc());
        holder.iv_imagen_btc.setImageResource(R.drawable.flores1);
        //Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
    }


    @Override
    public int getItemCount() {
        return listaBitacoras.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombre_btc, tv_descripcion_btc;
        ImageView iv_imagen_btc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombre_btc = itemView.findViewById(R.id.tv_nombre_btc);
            tv_descripcion_btc = itemView.findViewById(R.id.tv_descripcion_btc);
            iv_imagen_btc = itemView.findViewById(R.id.iv_imagen_btc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(listaBitacoras.get(posicion), posicion);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemLongClick(listaBitacoras.get(posicion), posicion);
                    }
                    return true;
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Bitacora documentSnapshot, int posicion);

        void onItemLongClick(Bitacora documentReference, int posicion);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}

