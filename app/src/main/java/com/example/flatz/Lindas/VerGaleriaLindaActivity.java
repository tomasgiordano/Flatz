package com.example.flatz.Lindas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.flatz.Adapter.GaleriaAdapter;
import com.example.flatz.HomeActivity;
import com.example.flatz.Pojos.Galeria;
import com.example.loginscreen.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerGaleriaLindaActivity extends AppCompatActivity {
    RecyclerView rv_galeria;
    GaleriaAdapter adapter;
    ImageView ivCamera;
    Button salir;
    ArrayList<Galeria> galeriaArrayList;
    LinearLayoutManager mlayoutManager;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_galeria_linda);

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");

        mlayoutManager = new LinearLayoutManager(this);
        mlayoutManager.setReverseLayout(true);
        mlayoutManager.setStackFromEnd(true);
        rv_galeria=findViewById(R.id.rv);
        rv_galeria.setLayoutManager(mlayoutManager);

        galeriaArrayList = new ArrayList<>();
        adapter = new GaleriaAdapter(galeriaArrayList,this);
        rv_galeria.setAdapter(adapter);

        ivCamera=findViewById(R.id.ivCamera);
        salir =findViewById(R.id.ivSalir);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(v.getContext(), AgregarFotoLindaActivity.class);
                p.putExtra("email",email);
                startActivity(p);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(v.getContext(), HomeActivity.class);
                p.putExtra("email",email);
                startActivity(p);
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("FotosLindasSubidas");
        ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    galeriaArrayList.removeAll(galeriaArrayList);
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {

                        Galeria gal = snapshot1.getValue(Galeria.class);

                        galeriaArrayList.add(gal);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}