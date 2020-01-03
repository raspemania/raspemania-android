package br.com.raspemania.model.entidade;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.model.BaseModel;

@IgnoreExtraProperties
public class Colaborador extends BaseModel implements Serializable {

    public String email;
    public String apelido;
    public String uid;

    /**
     * @see ConstantHelper
     */
    public long perfil;

    @NonNull
    @Override
    public String toString() {
        return apelido;
    }

    @Exclude
    public String getPerfil(){
        if (this.perfil == ConstantHelper.PERFIL_ADM) {
            return ConstantHelper.PERFIL_ADM_STR;
        }
        if (this.perfil == ConstantHelper.PERFIL_COLABORADOR) {
            return ConstantHelper.PERFIL_COLABORADOR_STR;
        }
        else {
            return "-";
        }
    }
}
