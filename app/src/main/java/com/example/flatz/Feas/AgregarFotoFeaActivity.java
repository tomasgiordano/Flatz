package com.example.flatz.Feas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flatz.HomeActivity;
import com.example.flatz.Lindas.VerGaleriaLindaActivity;
import com.example.flatz.Pojos.Galeria;
import com.example.loginscreen.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class AgregarFotoFeaActivity extends AppCompatActivity {

    Button btnSubir,btnSeleccionar;
    ImageView foto,home;
    Button salir;
    DatabaseReference imgref;
    StorageReference storageReference;
    ProgressDialog cargando;
    Bitmap thumb_bitmap=null;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_foto_fea);

        foto=findViewById(R.id.imgFoto);
        salir=findViewById(R.id.ivSalir);

        btnSeleccionar=findViewById(R.id.buttonSeleccionar);
        btnSubir=findViewById(R.id.buttonSubir);
        btnSeleccionar.setText("SELECCIONAR FOTO");
        btnSubir.setText("SUBIR FOTO");

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");

        home = findViewById(R.id.ivHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent p = new Intent(v.getContext(), VerGaleriaFeaActivity.class);
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

                imgref = FirebaseDatabase.getInstance().getReference().child("FotosFeasSubidas");
        storageReference = FirebaseStorage.getInstance().getReference().child("imgfea_comprimido");
        cargando = new ProgressDialog(this);

        btnSeleccionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                CropImage.startPickImageActivity(AgregarFotoFeaActivity.this);
            }
        });
    }//fin on create

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE&& resultCode== Activity.RESULT_OK)
        {
            Uri imageuri = CropImage.getPickImageResultUri(this,data);

            //RECORTAR IMAGEN

            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON).
                    setRequestedSize(720,576)
                    .setAspectRatio(2,1)
                    .start(AgregarFotoFeaActivity.this);

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                Uri resultUri=result.getUri();
                File url = new File(resultUri.getPath());

                Picasso.with(this).load(url).into(foto);

                //Comprimiendo Imagen

                try{
                    thumb_bitmap = new Compressor(this)
                    .setMaxWidth(720)
                    .setMaxHeight(576)
                    .setQuality(90)
                    .compressToBitmap(url);
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                //Fin del compresor

                int p = (int) (Math.random()*25+1);int s = (int) (Math.random()*25+1);
                int t = (int) (Math.random()*25+1);int c = (int) (Math.random()*25+1);
                int numero1 = (int) (Math.random() * 1012+2111);
                int numero2 = (int) (Math.random() * 1012+2111);

                String[] elementos ={"a","b","c","d","e","f","g","h","i","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
                final String aleatorio = elementos[p]+elementos[s]+numero1+elementos[t]+elementos[c]+numero2+"comprimido.jpg";
                btnSubir.setEnabled(true);

                btnSubir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cargando.setTitle("Subiendo Foto...");
                        cargando.setMessage("Espera por favor...");
                        cargando.show();

                        final StorageReference ref = storageReference.child("cosasFeas").child(aleatorio);
                        UploadTask uploadTask = ref.putBytes(thumb_byte);

                        //SUBIR IMAGEN EN STORAGE

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful())
                                {
                                    throw Objects.requireNonNull(task.getException());

                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloaduri = task.getResult();

                                Galeria gal = new Galeria(0,email,downloaduri.toString());

                                imgref.push().setValue(gal);
                                cargando.dismiss();

                                btnSubir.setEnabled(false);
                                foto.setImageResource(R.drawable.ic_agregarfoto);
                                Toast.makeText(AgregarFotoFeaActivity.this,"Imagen subida con exito",Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
            };

            }
        }
    }