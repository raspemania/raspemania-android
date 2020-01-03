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
import br.com.raspemania.model.entidade.Produto;
import br.com.raspemania.view.adapter.ProdutoAdapter;
import br.com.raspemania.viewmodel.ProdutoViewModel;



public class ProdutoActivity extends BaseActivity {

    private ProdutoViewModel mViewModel;
    private AppCompatButton btnSalvar;
    private TextInputEditText nomeProduto;
    private TextInputEditText valorProduto;
    private Produto produto;
    private Spinner mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        mViewModel = ViewModelProviders.of(this).get(ProdutoViewModel.class);

        doBindings();

        btnSalvar = findViewById(R.id.btn_salvar);
        nomeProduto = findViewById(R.id.nome_produto);
        valorProduto = findViewById(R.id.valor_produto);
        mStatus = findViewById(R.id.produto_status);
        //valorPproduto.addTextChangedListener(new MoneyTextWatcher(valorPproduto));

        super.spinnerStatus(mStatus);

        produto = new Produto();

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(ProdutoAdapter.TAG) != null) {
            bindCampos((Produto) getIntent().getExtras().getSerializable(ProdutoAdapter.TAG));
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camposValidos()){
                   return;
                }
                mViewModel.saveOrUpdate(produto());
            }
        });
    }

    private void doBindings(){
        super.onStart();
        super.observeError(mViewModel);
        observeSucess();
    }

    private void bindCampos(Produto itemLista){
        this.produto = itemLista;
        nomeProduto.setText(itemLista.nome);
        valorProduto.setText(Double.toString(itemLista.valor));
        mStatus.setSelection(super.setSpinner(itemLista.status));
    }

    private Boolean camposValidos(){
        if(TextUtils.isEmpty(nomeProduto.getText())){
            nomeProduto.setError(getString(R.string.erro_nome_produto));
            return false;
        }
        if(TextUtils.isEmpty(valorProduto.getText())){
            valorProduto.setError(getString(R.string.erro_valor_produto));
            return false;
        }
        if (!validStatus(mStatus)) {
            Toast.makeText(this, getString(R.string.erro_spinner_status), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Produto produto() {
        produto.nome = nomeProduto.getText().toString();
        produto.valor = Double.parseDouble(valorProduto.getText().toString());
        //produto.valor = Double.parseDouble(valorProduto.getText().toString().replace(".", "").replace(",", "."));
        produto.status = super.getStatusSpinner((String) mStatus.getSelectedItem());
        return produto;
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
