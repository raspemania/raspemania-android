package br.com.raspemania.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.raspemania.R;
import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.helper.CollectionHelper;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.helper.ErrorHelper;
import br.com.raspemania.helper.SharedPrefHelper;
import br.com.raspemania.model.entidade.Colaborador;

public class CadastroUsuarioActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CadastroUsuarioActivity";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mApelidoField;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        mApelidoField = findViewById(R.id.fieldApelido);

        findViewById(R.id.cadastrarBtn).setOnClickListener(this);
        findViewById(R.id.possuoCadastroText).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPasswordField.getText().clear();
        mEmailField.getText().clear();
        mApelidoField.getText().clear();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification();
                            saveColaborador(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            ErrorHelper.errorLogin(((FirebaseAuthException)task.getException()).getErrorCode(), CadastroUsuarioActivity.this, mEmailField, mPasswordField);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroUsuarioActivity.this,
                                    "Foi enviado um email de verificação para " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            //TODO alterar esse tratamento. passar ((FirebaseAuthException) task.getException()).getErrorCode()
                            ErrorHelper.errorLogin("GERAL", CadastroUsuarioActivity.this, mEmailField, mPasswordField);
                        }
                    }
                });
    }

    public void saveColaborador(FirebaseUser user) {

        this.db = FirebaseRaspeMania.getDatabase();

        DocumentReference ref = db.collection(CollectionHelper.COLLECTION_COLABORADOR).document();
        String myId = ref.getId();

        Colaborador mColaborador = new Colaborador();
        mColaborador.apelido = mApelidoField.getText().toString();
        mColaborador.email = user.getEmail();
        mColaborador.uid = user.getUid();
        mColaborador.key = myId;
        mColaborador.status = ConstantHelper.INATIVO;
        mColaborador.perfil = ConstantHelper.PERFIL_COLABORADOR;

        db.collection(CollectionHelper.COLLECTION_COLABORADOR).document(myId).set(mColaborador)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    doLogin();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    signOut();
                    ErrorHelper.errorLogin("ERROR_CADASTRO_USER", CadastroUsuarioActivity.this, null, null);
                }
            });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.campo_obrigatorio));
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError(getString(R.string.campo_obrigatorio));
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String apelido = mApelidoField.getText().toString();
        if (TextUtils.isEmpty(apelido)) {
            mApelidoField.setError(getString(R.string.campo_obrigatorio));
            valid = false;
        } else {
            mApelidoField.setError(null);
        }

        return valid;
    }
    private void doLogin() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void signOut(){
        hideProgressDialog();
        SharedPrefHelper.clearShared(this);
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cadastrarBtn) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.possuoCadastroText) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}