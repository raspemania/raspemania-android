package br.com.raspemania.firebase.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.helper.CollectionHelper;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.model.consulta.RelatorioConsulta;
import br.com.raspemania.model.entidade.Leitura;

public class LeituraRepository extends BaseRepository<Leitura> {

    static String TAG = "LEITURA_REPOSITORY";

    private String collection = CollectionHelper.COLLECTION_LEITURA;
    protected FirebaseFirestore db;
    public Leitura object;

    /**
     * Constructor
     */

    public LeituraRepository() {
        super(CollectionHelper.COLLECTION_LEITURA, Leitura.class);
    }

    /**
     *
     * @param entity
     * @return Task<Void>
     * @throws Exception
     */
    public Task<Void> saveRefId(Leitura entity) throws Exception {

        this.db = FirebaseRaspeMania.getDatabase();

        DocumentReference ref = db.collection(collection).document();
        String myId = ref.getId();

        this.object = entity;
        object.key = myId;
        object.status = ConstantHelper.ATIVO;

        return db.collection(collection).document(myId).set(object);
    }

    /**
     * Get all documents by collection with filters
     * @return
     * @throws Exception
     */
    public Task<QuerySnapshot> getAll(RelatorioConsulta filtros) throws Exception {

        this.db = FirebaseRaspeMania.getDatabase();

        Query query = db.collection(collection);

        if (filtros.cliente != null) {
            query = query.whereEqualTo("cliente.key", filtros.cliente.key);
        }
        if (filtros.rota!=null) {
            query = query.whereEqualTo("cliente.rota.key", filtros.rota.key);
        }
        if (filtros.colaborador!=null) {
            query = query.whereEqualTo("cliente.rota.colaborador.key", filtros.colaborador.key);
        }
        if (filtros.dataInicio!=null) {
            query = query.whereGreaterThanOrEqualTo("dataUltimaAtualizacao", filtros.dataInicio);
        }
        if (filtros.dataFim!=null) {
            query = query.whereLessThan("dataUltimaAtualizacao", filtros.dataFim);
        }

        return query.get();
    }
}
