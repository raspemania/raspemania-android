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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.raspemania.R;
import br.com.raspemania.firebase.FirebaseRaspeMania;
import br.com.raspemania.helper.CollectionHelper;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.helper.ErrorHelper;
import br.com.raspemania.helper.SharedPrefHelper;
import br.com.raspemania.model.entidade.Colaborador;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.cadastrarNovaConta).setOnClickListener(this);
        findViewById(R.id.esqueciSenha).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPasswordField.getText().clear();
        mEmailField.getText().clear();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        tryLogin(currentUser);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            tryLogin(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException().getCause());
                            ErrorHelper.errorLogin("GERAL", LoginActivity.this, mEmailField, mPasswordField);
                        }
                        if (!task.isSuccessful()) {
                            ErrorHelper.errorLogin(((FirebaseAuthException) task.getException()).getErrorCode(), LoginActivity.this, mEmailField, mPasswordField);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Campo obrigatório");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Campo obrigatório");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
    private void tryLogin(FirebaseUser user) {
        if (user != null) {
            try {
                showProgressDialog();
                getByEmail(user.getEmail());
            } catch (Exception e) {
                hideProgressDialog();
                e.printStackTrace();
                ErrorHelper.errorLogin("GERAL", LoginActivity.this, null, null);
            }
        }
    }

    private void doLogin(Colaborador colaborador) {
        SharedPrefHelper.setSharedOBJECT(this, ConstantHelper.COLABORADOR_PREF, colaborador);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void esqueciSenha(){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        final String emailAddress = mEmailField.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) {
            mEmailField.setError("Campo obrigatório");
        } else {
            mEmailField.setError(null);
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,
                                        "Foi enviado um email de recuperação de senha para " + emailAddress,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "erro ao recuperar senha", task.getException());
                                ErrorHelper.errorLogin("ERROR_RECUPERAR_SENHA", LoginActivity.this, null, null);
                            }
                        }
                    });
        }
    }

    public void getByEmail(String email) throws Exception {
        this.db = FirebaseRaspeMania.getDatabase();
        db.collection(CollectionHelper.COLLECTION_COLABORADOR)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Colaborador colaborador = document.toObject(Colaborador.class);
                            if(colaborador.status == ConstantHelper.INATIVO){
                                signOut();
                                ErrorHelper.errorLogin("INVALID_USER", LoginActivity.this, null, null);
                            } else {
                                doLogin(colaborador);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signOut();
                        ErrorHelper.errorLogin("ERROR_GET_USER", LoginActivity.this, null, null);
                    }
                });
    }

    public void signOut(){
        hideProgressDialog();
        SharedPrefHelper.clearShared(this);
        mAuth.signOut();
        //startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailSignInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.cadastrarNovaConta) {
            finish();
            startActivity(new Intent(this, CadastroUsuarioActivity.class));
        } else if (i == R.id.esqueciSenha) {
            esqueciSenha();
        }
    }
}