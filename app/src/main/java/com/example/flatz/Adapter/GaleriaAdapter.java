package com.example.flatz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flatz.Pojos.Galeria;
import com.example.loginscreen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.FotosViewHolder> {

    List<Galeria> galeriaList;
    Context context;
    DatabaseReference mDataBase;
    Boolean flag=true;

    public GaleriaAdapter(List<Galeria> galeriaList, Context context) {
        this.galeriaList = galeriaList;
        this.context = context;
        this.mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public FotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_galeria,parent,false);
        FotosViewHolder holder = new FotosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FotosViewHolder holder, int position) {
        final Galeria gal = galeriaList.get(position);
        holder.titulo.setText(gal.getEmail());
        holder.button.setText(String.valueOf(gal.getVotos()));

        Picasso.with(context).load(gal.getPath()).into(holder.img_foto, new Callback() {
            @Override
            public void onSuccess() {
                holder.progress.setVisibility(View.GONE);
                holder.img_foto.setVisibility(View.VISIBLE);

                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(flag)
                        {
                            gal.setVotos(gal.getVotos()+1);
                            holder.button.setText(String.valueOf(gal.getVotos()));
                            flag=!flag;
                        }
                        else
                        {
                            gal.setVotos(gal.getVotos()-1);
                            holder.button.setText(String.valueOf(gal.getVotos()));
                            flag=!flag;
                        }
                    }
                });
            }
            @Override
            public void onError() {
                Toast.makeText(context,"ERROR!",Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galeriaList.size();
    }

    public class FotosViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        ImageView img_foto;
        ProgressBar progress;
        Button button;
        public FotosViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tv_titulo);
            img_foto=itemView.findViewById(R.id.iv_imagen);
            progress=itemView.findViewById(R.id.progress_bar_galeria);
            button = itemView.findViewById(R.id.btn_votos);
        }
    }
}
