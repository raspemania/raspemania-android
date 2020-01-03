package br.com.raspemania.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.helper.SpinnerHelper;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.model.entidade.Rota;
import br.com.raspemania.view.adapter.RotaAdapter;
import br.com.raspemania.viewmodel.ColaboradorViewModel;
import br.com.raspemania.viewmodel.RotaViewModel;

public class RotaActivity extends BaseActivity {

    private RotaViewModel mViewModel;
    private AppCompatButton btnSalvar;
    private TextInputEditText mRotaDescricao;
    private Spinner mRotaColaborador;
    private Spinner mStatus;
    private Rota mRota;
    private ColaboradorViewModel mViewModelColaborador;
    private ArrayAdapter<Colaborador> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rota);

        mViewModel = ViewModelProviders.of(this).get(RotaViewModel.class);
        mViewModelColaborador = ViewModelProviders.of(this).get(ColaboradorViewModel.class);

        doBindings();

        btnSalvar = findViewById(R.id.btn_salvar);
        mRotaDescricao = findViewById(R.id.rota_descricao);
        mRotaColaborador = findViewById(R.id.rota_colaborador);
        mStatus = findViewById(R.id.rota_status);

        super.spinnerStatus(mStatus);

        mRota = new Rota();

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(RotaAdapter.TAG) != null) {
            bindCampos((Rota) getIntent().getExtras().getSerializable(RotaAdapter.TAG));
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!camposValidos()) {
                    return;
                }
                mViewModel.saveOrUpdate(rota());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModelColaborador.getAllSpinner();
    }

    private void doBindings() {
        super.onStart();
        super.observeError(mViewModel);
        observeSucess();
        observeGetAllColaborador();
    }

    private void bindCampos(Rota itemLista) {
        this.mRota = itemLista;
        mRotaDescricao.setText(itemLista.nome);
        mStatus.setSelection(super.setSpinner(itemLista.status));
    }

    private Boolean camposValidos() {
        if (TextUtils.isEmpty(mRotaDescricao.getText())) {
            mRotaDescricao.setError(getString(R.string.erro_rota_descricao));
            return false;
        }

        Colaborador c = (Colaborador) mRotaColaborador.getSelectedItem();
        if (c.key == null || c.key.isEmpty()) {
            Toast.makeText(this, getString(R.string.erro_rota_colaborador), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!validStatus(mStatus)) {
            Toast.makeText(this, getString(R.string.erro_spinner_status), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Rota rota() {
        mRota.nome = mRotaDescricao.getText().toString();
        mRota.colaborador = (Colaborador) mRotaColaborador.getSelectedItem();
        mRota.status = super.getStatusSpinner((String) mStatus.getSelectedItem());
        return mRota;
    }

    private void observeSucess() {
        mViewModel.sucess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void bindSpinner(Colaborador col) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).email.equalsIgnoreCase(col.email)) {
                mRotaColaborador.setSelection(i);
                break;
            }
        }
    }

    private void observeGetAllColaborador() {

        mViewModelColaborador.mList.observe(this, new Observer<List<Colaborador>>() {
            @Override
            public void onChanged(List<Colaborador> resultList) {
                adapter = new ArrayAdapter<>(RotaActivity.this, R.layout.item_spinner_default, SpinnerHelper.spinnerColaborador(resultList, RotaActivity.this));
                mRotaColaborador.setAdapter(adapter);
                if (mRota.colaborador != null) bindSpinner(mRota.colaborador);
            }
        });
    }

}
