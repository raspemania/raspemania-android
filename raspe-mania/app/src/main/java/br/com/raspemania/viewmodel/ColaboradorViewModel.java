package br.com.raspemania.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.raspemania.firebase.repository.ColaboradorRepository;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.model.entidade.Colaborador;

public class ColaboradorViewModel extends BaseViewModel {

    static String TAG = "COLABORADOR_VIEW_MODEL";

    private ColaboradorRepository service = new ColaboradorRepository();

    public MutableLiveData<String> sucess;
    public MutableLiveData<List<Colaborador>> mList;
    public MutableLiveData<Colaborador> mColaborador;

    public ColaboradorViewModel() {
        sucess = new MutableLiveData<>();
        error = new MutableLiveData<>();
        mList = new MutableLiveData<List<Colaborador>>();
    }

    /**
     * Sava ou atualiza um objeto
     *
     * @param obj
     */
    public void saveOrUpdate(Colaborador obj) {
        if (obj.key == null) {
            save(obj);
        } else {
            update(obj);
        }
    }

    /**
     * Add a new document with a key
     *
     * @param obj
     */
    private void save(Colaborador obj) {
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
                    });
            ;
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao salvar!");
        }
    }

    /**
     * Update document existent
     *
     * @param obj
     */
    private void update(Colaborador obj) {
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
                    });
            ;
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao atualizar");
        }
    }

    /**
     * Update a document - set status Inativo
     *
     * @param obj
     */
    public void delete(Colaborador obj) {

        try {
            obj.status = ConstantHelper.INATIVO;
            service.update(obj, obj.key)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Salvo com sucesso!");
                            sucess.setValue("Status atualizado com sucesso!");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Erro ao atualizar", e);
                            error.setValue("Erro ao atualizar");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao atualizar");
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
                            mList.setValue(querySnapshot.toObjects(Colaborador.class));
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

    public void getAllSpinner() {
        try {

            service.getAll("status", ConstantHelper.ATIVO)
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            Log.d(TAG, "Listou todos!");
                            mList.setValue(querySnapshot.toObjects(Colaborador.class));
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