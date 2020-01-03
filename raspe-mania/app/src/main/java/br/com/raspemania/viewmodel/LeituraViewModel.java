package br.com.raspemania.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.firebase.repository.LeituraRepository;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.helper.DateHelper;
import br.com.raspemania.model.consulta.RelatorioConsulta;
import br.com.raspemania.model.entidade.Leitura;

public class LeituraViewModel extends BaseViewModel {

    static String TAG = "LEITURA_VIEW_MODEL";

    private LeituraRepository service = new LeituraRepository();

    public MutableLiveData<String> sucess;
    public MutableLiveData<List<Leitura>> mList;

    public LeituraViewModel() {
        sucess = new MutableLiveData<>();
        error = new MutableLiveData<>();
        mList = new MutableLiveData<List<Leitura>>();
    }

    /**
     * Sava ou atualiza um objeto
     *
     * @param obj
     */
    public void saveOrUpdate(Leitura obj) {
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
    private void save(Leitura obj) {
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
    private void update(Leitura obj) {
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
     * Delete a document
     *
     * @param obj
     */
    public void delete(Leitura obj) {
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
                    });
            ;
        } catch (Exception e) {
            e.printStackTrace();
            error.setValue("Erro ao deletar!");
        }
    }

    /**
     * Get all examples
     */
    /*public void getAll() {
        try {
            service.getAll()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            Log.d(TAG, "Listou todos!");
                            mList.setValue(querySnapshot.toObjects(Leitura.class));
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
    }*/

    public void getAllSpinnerForUser() {
        try {

            service.getAllReference()
                    .whereEqualTo("cliente.rota.colaborador.email", FirebaseRaspeMania.getEMailUsuario())
                    .whereEqualTo("status", ConstantHelper.ATIVO)
                    .whereGreaterThanOrEqualTo("dataUltimaAtualizacao", DateHelper.addData(new Date(), -1))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            Log.d(TAG, "Listou todos!");
                            mList.setValue(querySnapshot.toObjects(Leitura.class));
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

    public void getAllLastDay() {
        try {

            service.getAllReference()
                    .whereGreaterThanOrEqualTo("dataUltimaAtualizacao", DateHelper.addData(new Date(), -1))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            Log.d(TAG, "Listou todos!");
                            mList.setValue(querySnapshot.toObjects(Leitura.class));
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

    /**
     * Get all examples with filters
     */
    public void getAll(RelatorioConsulta filtros) {
        try {
            service.getAll(filtros)
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            Log.d(TAG, "Listou todos!");
                            mList.setValue(querySnapshot.toObjects(Leitura.class));
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