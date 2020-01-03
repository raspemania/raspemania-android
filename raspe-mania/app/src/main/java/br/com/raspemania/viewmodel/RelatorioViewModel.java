package br.com.raspemania.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.raspemania.firebase.repository.RelatorioRepository;
import br.com.raspemania.model.consulta.RelatorioConsulta;

public class RelatorioViewModel extends BaseViewModel {

    static String TAG = "RELATORIO_VIEW_MODEL";

    private RelatorioRepository service = new RelatorioRepository();

    public MutableLiveData<String> sucess;
    public MutableLiveData<List<RelatorioConsulta>> mList;

    public RelatorioViewModel() {
        sucess = new MutableLiveData<>();
        error = new MutableLiveData<>();
        mList = new MutableLiveData<List<RelatorioConsulta>>();
    }

    /**
     * Sava ou atualiza um objeto
     * @param obj
     */
    public void saveOrUpdate(RelatorioConsulta obj) {
        if(obj.key == null){
            save(obj);
        } else {
            update(obj);
        }
    }

    /**
     * Add a new document with a key
     * @param obj
     */
    private void save(RelatorioConsulta obj) {
        try {
            service.saveRefId(obj)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Salvo com sucesso!");
                            sucess.setValue("Salvo com sucesso!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Erro ao salvar!", e);
                            error.setValue("Erro ao salvar!");
                        }
                    });;
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao salvar!");
        }
    }

    /**
     * Update document existent
     * @param obj
     */
    private void update(RelatorioConsulta obj){
        try {
            service.update(obj, obj.key)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Salvo com sucesso!");
                            sucess.setValue("Atualizado com sucesso!");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Erro ao atualizar", e);
                            error.setValue("Erro ao atualizar");
                        }
                    });;
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao atualizar");
        }
    }

    /**
     * Delete a document
     * @param obj
     */
    public void delete(RelatorioConsulta obj){
        try {
            service.delete(obj.key)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Deletado com sucesso!");
                            sucess.setValue("Deletado com sucesso!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Erro ao deletar!", e);
                            error.setValue("Erro ao deletar!");
                        }
                    });;
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao deletar!");
        }
    }

    /**
     * Get all examples
     */
    public void getAll() {
        try {
            service.getAll()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            Log.d(TAG, "Listou todos!");
                            mList.setValue(querySnapshot.toObjects(RelatorioConsulta.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Erro ao listar!", e);
                            error.setValue("Erro ao listar!");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao listar!");
        }
    }
}