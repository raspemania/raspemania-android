package br.com.raspemania.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import br.com.raspemania.R;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.view.adapter.ColaboradorAdapter;
import br.com.raspemania.viewmodel.ColaboradorViewModel;

public class ColaboradorActivity extends BaseActivity {

    private ColaboradorViewModel mViewModel;
    private AppCompatButton btnSalvar;
    private TextInputEditText mNome;
    private TextInputEditText mApelido;
    private Colaborador mColaborador;
    private Spinner mStatus;
    private Spinner mPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colaborador);

        mViewModel = ViewModelProviders.of(this).get(ColaboradorViewModel.class);

        doBindings();

        btnSalvar = findViewById(R.id.btn_salvar_colaborador);
        mNome = findViewById(R.id.email_colaborador);
        mApelido = findViewById(R.id.apelido_colaborador);
        mStatus = findViewById(R.id.colaborador_status);
        mPerfil = findViewById(R.id.colaborador_perfil);

        super.spinnerStatus(mStatus);
        super.spinnerPerfil(mPerfil);

        mColaborador = new Colaborador();

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(ColaboradorAdapter.TAG) != null) {
            bindCampos((Colaborador) getIntent().getExtras().getSerializable(ColaboradorAdapter.TAG));
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camposValidos()){
                    return;
                }
                mViewModel.saveOrUpdate(colaborador());
            }
        });
    }

    private void doBindings(){
        super.onStart();
        super.observeError(mViewModel);
        observeSucess();
    }

    private void bindCampos(Colaborador itemLista){
        this.mColaborador = itemLista;
        mNome.setText(itemLista.email);
        mApelido.setText(itemLista.apelido);
        mStatus.setSelection(super.setSpinner(itemLista.status));
        mPerfil.setSelection(super.setSpinner(itemLista.perfil));
    }

    private Boolean camposValidos(){
        if(TextUtils.isEmpty(mNome.getText())){
            mNome.setError(getString(R.string.erro_nome_colaborador));
            return false;
        }
        if(TextUtils.isEmpty(mApelido.getText())){
            mApelido.setError(getString(R.string.erro_apelido_colaborador));
            return false;
        }
        if (!validStatus(mStatus)) {
            Toast.makeText(this, getString(R.string.erro_spinner_status), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Colaborador colaborador() {
        mColaborador.email = mNome.getText().toString();
        mColaborador.apelido = mApelido.getText().toString();
        mColaborador.status = super.getStatusSpinner((String) mStatus.getSelectedItem());
        mColaborador.perfil = super.getPerfilSpinner((String) mPerfil.getSelectedItem());
        return mColaborador;
    }

    private void observeSucess(){
        mViewModel.sucess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                //Snackbar.make(view, "Nova leitura", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }
        });
    }
}