package br.com.raspemania.firebase.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.helper.ConstantHelper;

public abstract class BaseRepository<T> {

    static String TAG = "FIREBASE_REPOSITORY";

    private String collection;
    protected FirebaseFirestore db;
    //public T object;
    private Class<T> clazz;

    /**
     * Constructor
     *
     * @param collection
     * @param clazz
     */
    public BaseRepository(String collection, Class<T> clazz) {
        this.db = FirebaseRaspeMania.getDatabase();
        this.collection = collection;
        this.clazz = clazz;
    }

    /**
     * Get all documents by collection
     *
     * @return
     * @throws Exception
     */
    public Task<QuerySnapshot> getAll() throws Exception {
        return db.collection(collection)
                //.whereEqualTo()
                .get();
    }

    public Task<QuerySnapshot> getAll(String campo, Object valor) throws Exception {
        return db.collection(collection)
                .whereEqualTo(campo, valor)
                //.whereEqualTo("status", ConstantHelper.ATIVO)
                .get();
    }

    public CollectionReference getAllReference( ) throws Exception {
        return db.collection(collection);

    }


    /**
     * Update document existent
     *
     * @param entity
     * @param key
     * @return
     */
    public Task<Void> update(final T entity, final String key) {
        return db.collection(collection).document(key).set(entity, SetOptions.merge());
    }

    /**
     * Delete a document
     *
     * @param key
     * @return
     */
    public Task<Void> delete(final String key) {
        return db.collection(collection).document(key).delete();
    }


}
