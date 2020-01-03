package br.com.raspemania.firebase.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.helper.CollectionHelper;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.model.entidade.Cliente;

public class ClienteRepository extends BaseRepository<Cliente> {

    static String TAG = "CLIENTE _REPOSITORY";

    private String collection = CollectionHelper.COLLECTION_CLIENTE;
    protected FirebaseFirestore db;
    public Cliente object;

    /**
     * Constructor
     */

    public ClienteRepository() {
        super(CollectionHelper.COLLECTION_CLIENTE, Cliente.class);
    }

    /**
     *
     * @param entity
     * @return Task<Void>
     * @throws Exception
     */
    public Task<Void> saveRefId(Cliente entity) throws Exception {

        this.db = FirebaseRaspeMania.getDatabase();

        DocumentReference ref = db.collection(collection).document();
        String myId = ref.getId();

        this.object = entity;
        object.key = myId;
        object.status = ConstantHelper.ATIVO;

        return db.collection(collection).document(myId).set(object);
    }
}
