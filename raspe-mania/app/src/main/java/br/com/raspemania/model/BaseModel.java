package br.com.raspemania.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {

    public String key;
    public long status;                 //1 ativo or 2 inativo

    @ServerTimestamp
    public Date dataUltimaAtualizacao;


}
