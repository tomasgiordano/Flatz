package com.example.flatz.Pojos;

import com.google.firebase.database.DatabaseReference;

public class Galeria {
    public int votos;
    public String email;
    public String path;

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPath() {
        return path;
    }

    public Galeria()
    {

    }

    public Galeria(int votos, String email, String path)
    {
        this.votos=votos;
        this.email=email;
        this.path=path;
    }
}
