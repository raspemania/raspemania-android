package br.com.raspemania.model.entidade;

import androidx.annotation.NonNull;

import java.io.Serializable;

import br.com.raspemania.model.BaseModel;

public class Rota extends BaseModel implements Serializable {

    public String nome;
    public Colaborador colaborador;

    @NonNull
    @Override
    public String toString() {
        return nome;
    }
}
