package br.com.raspemania.firebase.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.helper.CollectionHelper;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.model.consulta.RelatorioConsulta;

public class RelatorioRepository extends BaseRepository<RelatorioConsulta> {

    static String TAG = "RELATORIO_REPOSITORY";

    private String collection = CollectionHelper.COLLECTION_COLABORADOR;
    protected FirebaseFirestore db;
    public RelatorioConsulta object;

    /**
     * Constructor
     */

    public RelatorioRepository() {
        super(CollectionHelper.COLLECTION_COLABORADOR, RelatorioConsulta.class);
    }

    /**
     *
     * @param entity
     * @return Task<Void>
     * @throws Exception
     */
    public Task<Void> saveRefId(RelatorioConsulta entity) throws Exception {

        this.db = FirebaseRaspeMania.getDatabase();

        DocumentReference ref = db.collection(collection).document();
        String myId = ref.getId();

        this.object = entity;
        object.key = myId;
        object.status = ConstantHelper.ATIVO;

        return db.collection(collection).document(myId).set(object);
    }
}
