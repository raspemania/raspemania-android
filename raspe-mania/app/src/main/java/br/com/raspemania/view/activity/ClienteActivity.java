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
import br.com.raspemania.model.entidade.Cliente;
import br.com.raspemania.model.entidade.Rota;
import br.com.raspemania.view.adapter.ClienteAdapter;
import br.com.raspemania.viewmodel.ClienteViewModel;
import br.com.raspemania.viewmodel.RotaViewModel;

public class ClienteActivity extends BaseActivity {

    private ClienteViewModel mViewModel;
    private RotaViewModel mViewModelRota;
    private AppCompatButton btnSalvar;
    private TextInputEditText mClienteEstoque;
    private TextInputEditText mClienteEndereco;
    private TextInputEditText mClienteCodigo;
    private TextInputEditText mClientePorcentagem;
    private TextInputEditText mNomeCliente;
    private Spinner mClienteRota;
    private Spinner mStatus;
    private ArrayAdapter<Rota> adapter;
    private Cliente mCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        mViewModel = ViewModelProviders.of(this).get(ClienteViewModel.class);
        mViewModelRota = ViewModelProviders.of(this).get(RotaViewModel.class);

        doBindings();

        btnSalvar = findViewById(R.id.btn_salvar);
        mClienteEstoque = findViewById(R.id.cliente_estoque);
        mClienteEndereco = findViewById(R.id.cliente_endereco);
        mClienteCodigo = findViewById(R.id.cliente_codigo);
        mClientePorcentagem = findViewById(R.id.cliente_porcentagem);
        mClienteRota = findViewById(R.id.cliente_rota);
        mStatus = findViewById(R.id.cliente_status);
        mNomeCliente = findViewById(R.id.cliente_nome);

        super.spinnerStatus(mStatus);

        mCliente = new Cliente();

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(ClienteAdapter.TAG) != null) {
            bindCampos((Cliente) getIntent().getExtras().getSerializable(ClienteAdapter.TAG));
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!camposValidos()) {
                    return;
                }
                mViewModel.saveOrUpdate(cliente());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModelRota.getAllSpinner();
    }

    private void doBindings() {
        super.onStart();
        super.observeError(mViewModel);
        observeSucess();
        observeGetAllRotas();
    }

    private void bindCampos(Cliente itemLista) {
        this.mCliente = itemLista;
        mClienteEstoque.setText(String.valueOf(itemLista.estoque));
        mClienteEstoque.setEnabled(false);
        mClienteEstoque.setTextColor(getResources().getColor(R.color.grey_500));
        mClienteEndereco.setText(itemLista.endereco);
        mClienteCodigo.setText(itemLista.codigo);
        mClientePorcentagem.setText(String.valueOf(itemLista.porcentagem));
        mStatus.setSelection(super.setSpinner(itemLista.status));
        mNomeCliente.setText(itemLista.nome);
    }

    private Boolean camposValidos() {
        if (TextUtils.isEmpty(mClienteEstoque.getText())) {
            mClienteEstoque.setError(getString(R.string.erro_cliente_estoque));
            return false;
        }
        if (TextUtils.isEmpty(mNomeCliente.getText())) {
            mClienteEstoque.setError(getString(R.string.erro_cliente_nome));
            return false;
        }
        if (TextUtils.isEmpty(mClienteEndereco.getText())) {
            mClienteEndereco.setError(getString(R.string.erro_cliente_endereco));
            return false;
        }
        if (TextUtils.isEmpty(mClienteCodigo.getText())) {
            mClienteCodigo.setError(getString(R.string.erro_cliente_codigo));
            return false;
        }
        if (TextUtils.isEmpty(mClientePorcentagem.getText())) {
            mClientePorcentagem.setError(getString(R.string.erro_cliente_porcentagem));
            return false;
        }
        if (!validStatus(mStatus)) {
            Toast.makeText(this, getString(R.string.erro_spinner_status), Toast.LENGTH_SHORT).show();
            return false;
        }

        Rota r = (Rota) mClienteRota.getSelectedItem();
        if (r.key == null || r.key.isEmpty()) {
            Toast.makeText(this, getString(R.string.spinner_rota_erro), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Cliente cliente() {
        mCliente.estoque = Integer.parseInt(mClienteEstoque.getText().toString());
        mCliente.porcentagem = Double.parseDouble(mClientePorcentagem.getText().toString());
        mCliente.codigo = mClienteCodigo.getText().toString();
        mCliente.endereco = mClienteEndereco.getText().toString();
        mCliente.nome = mNomeCliente.getText().toString();
        mCliente.rota = (Rota) mClienteRota.getSelectedItem();
        mCliente.status = super.getStatusSpinner((String) mStatus.getSelectedItem());
        return mCliente;
    }

    private void observeSucess() {
        mViewModel.sucess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                //Snackbar.make(view, "Nova leitura", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }
        });
    }

    public void bindSpinner(Rota rota) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).nome.equalsIgnoreCase(rota.nome)) {
                mClienteRota.setSelection(i);
                break;
            }
        }
    }

    private void observeGetAllRotas() {

        mViewModelRota.mList.observe(this, new Observer<List<Rota>>() {
            @Override
            public void onChanged(List<Rota> resultList) {
                adapter = new ArrayAdapter<>(ClienteActivity.this, R.layout.item_spinner_default, SpinnerHelper.spinnerRota( resultList, ClienteActivity.this));
                mClienteRota.setAdapter(adapter);
                if (mCliente.rota != null) bindSpinner(mCliente.rota);
            }
        });
    }


}
