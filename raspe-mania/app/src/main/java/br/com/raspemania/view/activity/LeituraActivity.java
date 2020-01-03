package br.com.raspemania.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.helper.SharedPrefHelper;
import br.com.raspemania.helper.SpinnerHelper;
import br.com.raspemania.model.entidade.Cliente;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.model.entidade.Leitura;
import br.com.raspemania.model.entidade.PremiacaoList;
import br.com.raspemania.model.entidade.Produto;
import br.com.raspemania.view.adapter.PremiacaoAdapter;
import br.com.raspemania.viewmodel.ClienteViewModel;
import br.com.raspemania.viewmodel.LeituraViewModel;
import br.com.raspemania.viewmodel.ProdutoViewModel;

public class LeituraActivity extends BaseActivity {

    private Spinner spinnerProduto;
    private Spinner spinnerCliente;
    private TextInputEditText textQuantidadeVendida;
    private TextInputEditText textQuantidadeReposicao;
    private AppCompatButton btnSalvar;
    private AppCompatImageButton btnAdicionar;
    private AppCompatEditText textQuantidade;
    private AppCompatEditText textValor;

    private ProdutoViewModel mViewModelProduto;
    private ClienteViewModel mViewModelCliente;
    private LeituraViewModel mViewModelLeitura;
    private Leitura leitura;

    private RecyclerView mRecyclerView;
    private PremiacaoAdapter mAdapter;

    private AlertDialog alertDialog;

    private Colaborador mColaborador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitura);

        mColaborador = SharedPrefHelper.getSharedOBJECT(this, ConstantHelper.COLABORADOR_PREF, Colaborador.class);

        textQuantidade = findViewById(R.id.add_qtd_premiacao);
        textValor = findViewById(R.id.add_valor_premiacao);
        //textValor.addTextChangedListener(new MoneyTextWatcher(textValor));

        spinnerProduto = findViewById(R.id.spinnerProduto);
        spinnerCliente = findViewById(R.id.spinnerCliente);
        textQuantidadeVendida = findViewById(R.id.textQuantidadeVendida);
        textQuantidadeReposicao = findViewById(R.id.textQuantidadeReposicao);
        btnSalvar = findViewById(R.id.btn_salvar);
        btnAdicionar = findViewById(R.id.btn_adicionar);

        mViewModelProduto = ViewModelProviders.of(this).get(ProdutoViewModel.class);
        mViewModelCliente = ViewModelProviders.of(this).get(ClienteViewModel.class);
        mViewModelLeitura = ViewModelProviders.of(this).get(LeituraViewModel.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        doBindings();

        leitura = new Leitura();
        leitura.premiacaoList = new ArrayList<PremiacaoList>();

        btnAdicionar.setOnClickListener(clickAdicionarPremiacao);
        btnSalvar.setOnClickListener(clickSalvarPremiacao);




    }

    private View.OnClickListener clickAdicionarPremiacao = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Boolean add = true;

            if (TextUtils.isEmpty(textQuantidade.getText())) {
                textQuantidade.setError(getString(R.string.erro_leitura_qtd));
                add = false;
            }

            if (TextUtils.isEmpty(textValor.getText())) {
                textValor.setError(getString(R.string.erro_leitura_valor));
                add = false;
            }

            if (add) {
                PremiacaoList premiacaoList = new PremiacaoList();
                premiacaoList.quantidadePremiada = Integer.parseInt(textQuantidade.getText().toString());
                premiacaoList.valorPremiado = Double.parseDouble(textValor.getText().toString());
                //premiacaoList.valorPremiado = Double.parseDouble(textValor.getText().toString().replace(".", "").replace(",", "."));
                leitura.premiacaoList.add(premiacaoList);

                prepareRecyclerView(leitura.premiacaoList);

                textQuantidade.getText().clear();
                textValor.getText().clear();
            }
        }
    };

    private View.OnClickListener clickSalvarPremiacao = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (!camposValidos()) {
                return;
            }

            final Leitura leituraAux = leitura();

            LayoutInflater li = getLayoutInflater();

            View viewDialog = li.inflate(R.layout.dialog_premiacao, null);
            AppCompatTextView valorQuantidadeVendida = viewDialog.findViewById(R.id.valorQuantidadeVendida);
            AppCompatTextView valorReposicao = viewDialog.findViewById(R.id.valorReposicao);
            AppCompatTextView valorPremiacao = viewDialog.findViewById(R.id.valorPremiacao);
            AppCompatTextView valorRetirado = viewDialog.findViewById(R.id.valorRetirado);

            valorQuantidadeVendida.setText(String.valueOf(leituraAux.quantidadeVendida));
            valorReposicao.setText(String.valueOf(leituraAux.quantidadeReposicao));
            valorPremiacao.setText(leituraAux.getPremiacao());
            valorRetirado.setText(leituraAux.getValorRetirado());

            viewDialog.findViewById(R.id.btn_cancelar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            viewDialog.findViewById(R.id.btn_salvar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewModelLeitura.saveOrUpdate(leituraAux);
                    Cliente cli = leituraAux.cliente;
                    cli.estoque = cli.estoque - leituraAux.quantidadeVendida + leituraAux.quantidadeReposicao;
                    mViewModelCliente.saveOrUpdate(cli);
                    alertDialog.dismiss();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(LeituraActivity.this);
            builder.setView(viewDialog);
            alertDialog = builder.create();
            alertDialog.show();

        }
    };

    private Leitura leitura() {

        Cliente cliente  = (Cliente) spinnerCliente.getSelectedItem();
        Produto produto = (Produto) spinnerProduto.getSelectedItem();

        leitura.cliente = cliente;
        leitura.produto = produto;
        leitura.quantidadeReposicao = Integer.parseInt(textQuantidadeReposicao.getText().toString());
        leitura.quantidadeVendida = Integer.parseInt(textQuantidadeVendida.getText().toString());
        leitura.porcentagemClienteLeitura = cliente.porcentagem;
        leitura.valorProdutoLeitura = produto.valor;

        return leitura;
    }

    private Boolean camposValidos() {

        if (TextUtils.isEmpty(textQuantidadeVendida.getText())) {
            textQuantidadeVendida.setError(getString(R.string.erro_leitura_qtd_vendide));
            return false;
        }

        if (TextUtils.isEmpty(textQuantidadeReposicao.getText())) {
            textQuantidadeReposicao.setError(getString(R.string.erro_leitura_reposicao));
            return false;
        }

        Produto p = (Produto) spinnerProduto.getSelectedItem();
        if (p.key == null || p.key.isEmpty()) {
            Toast.makeText(this, getString(R.string.leitura_spinner_erro_produto), Toast.LENGTH_LONG).show();
            return false;
        }


        Cliente p2 = (Cliente) spinnerCliente.getSelectedItem();
        if (p2.key == null || p2.key.isEmpty()) {
            Toast.makeText(this, getString(R.string.leitura_spinner_erro_cliente), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
       // mViewModelProduto.getAll();
     //   mViewModelCliente.getAll();

        if (mColaborador.perfil == ConstantHelper.PERFIL_ADM) {
            mViewModelCliente.Spinner();
        } else {
            mViewModelCliente.getAllUsuario();
        }

        mViewModelProduto.getAllSpinner();

    }

    private void doBindings() {
        super.onStart();
        super.observeError(mViewModelLeitura);
        super.observeError(mViewModelProduto);
        super.observeError(mViewModelCliente);
        observeSucessSpinnerProduto();
        observeSucessSpinnerCliente();
        observeSucess();
    }

    private void observeSucessSpinnerProduto() {

        mViewModelProduto.mList.observe(this, new Observer<List<Produto>>() {
            @Override
            public void onChanged(List<Produto> resultList) {

                ArrayAdapter<Produto> adapter = new ArrayAdapter<>(LeituraActivity.this, R.layout.item_spinner_default, SpinnerHelper.spinnerProduto(resultList, LeituraActivity.this));
                spinnerProduto.setAdapter(adapter);

            }
        });
    }

    private void observeSucessSpinnerCliente() {

        mViewModelCliente.mList.observe(this, new Observer<List<Cliente>>() {
            @Override
            public void onChanged(List<Cliente> resultList) {

                ArrayAdapter<Cliente> adapter = new ArrayAdapter<>(LeituraActivity.this, R.layout.item_spinner_default, SpinnerHelper.spinnerCliente(resultList, LeituraActivity.this));
                spinnerCliente.setAdapter(adapter);
            }
        });
    }

    private void observeSucess() {
        mViewModelLeitura.sucess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void prepareRecyclerView(List<PremiacaoList> premiacoes) {
        mAdapter = new PremiacaoAdapter(premiacoes);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        hideProgressDialog();
    }

}
