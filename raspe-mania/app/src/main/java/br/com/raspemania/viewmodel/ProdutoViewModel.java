package br.com.raspemania.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.firebase.repository.ProdutoRepository;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.model.entidade.Produto;

public class ProdutoViewModel extends BaseViewModel {

    static String TAG = "PRODUTO_VIEW_MODEL";

    private ProdutoRepository service = new ProdutoRepository();

    public MutableLiveData<String> sucess;
    public MutableLiveData<List<Produto>> mList;

    public ProdutoViewModel() {
        sucess = new MutableLiveData<>();
        error = new MutableLiveData<>();
        mList = new MutableLiveData<List<Produto>>();
    }

    /**
     * Sava ou atualiza um objeto
     *
     * @param obj
     */
    public void saveOrUpdate(Produto obj) {
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
    private void save(Produto obj) {
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
    private void update(Produto obj) {
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
     * @param obj
     */
    /*public void delete(Produto obj){
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
    }*/

    /**
     * Update a document - set status Inativo
     *
     * @param obj
     */
    public void delete(Produto obj) {

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
            ;
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
                            mList.setValue(querySnapshot.toObjects(Produto.class));
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
                            mList.setValue(querySnapshot.toObjects(Produto.class));
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