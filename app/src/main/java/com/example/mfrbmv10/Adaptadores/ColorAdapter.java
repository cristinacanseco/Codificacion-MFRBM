package com.example.mfrbmv10.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfrbmv10.Modelos.ColorMuestreo;
import com.example.mfrbmv10.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ColorMuestreo> colorLista;

    public ColorAdapter(Context context, ArrayList<ColorMuestreo> colorLista) {
        this.context = context;
        this.colorLista = colorLista;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_colores, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ColorMuestreo color = colorLista.get(position);
        holder.tv_clase_color.setText(color.getClase());

        int r = (int) color.getColores()[0];
        int g = (int) color.getColores()[1];
        int b = (int) color.getColores()[2];
        String colores = "RGB("+r+","+g+","+ b+")";

        holder.tv_rgb_color.setText(colores);
        holder.tv_rgb_color.setTextColor(Color.rgb(r,g,b));
    }


    @Override
    public int getItemCount() {
        return colorLista.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_clase_color,tv_rgb_color;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_clase_color = itemView.findViewById(R.id.tv_clase_color);
            tv_rgb_color = itemView.findViewById(R.id.tv_rgb_color);
        }
    }
}
