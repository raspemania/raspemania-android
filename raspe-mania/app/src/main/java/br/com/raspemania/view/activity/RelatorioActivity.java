package br.com.raspemania.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.model.consulta.RelatorioConsulta;
import br.com.raspemania.model.entidade.Leitura;
import br.com.raspemania.view.adapter.RelatorioAdapter;
import br.com.raspemania.view.fragment.RelatorioFragment;
import br.com.raspemania.viewmodel.LeituraViewModel;

public class RelatorioActivity extends BaseActivity {


    private AppCompatTextView mValorQuantidadeVendidaResumo;
    private AppCompatTextView mValorPremiacaoResumo;
    private AppCompatTextView mValorRetiradoResumo;

    private RecyclerView mRecyclerView;
    private RelatorioAdapter mAdapter;

    private LeituraViewModel mViewModelLeitura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        mValorQuantidadeVendidaResumo = findViewById(R.id.valorQuantidadeVendidaResumo);
        mValorPremiacaoResumo = findViewById(R.id.valorPremiacaoResumo);
        mValorRetiradoResumo = findViewById(R.id.valorRetiradoResumo);

        mViewModelLeitura = ViewModelProviders.of(this).get(LeituraViewModel.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        doBindings();

        showProgressDialog();

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(RelatorioFragment.TAG) != null) {
            RelatorioConsulta aux = (RelatorioConsulta) getIntent().getExtras().getSerializable(RelatorioFragment.TAG);
            mViewModelLeitura.getAll(aux);
        }

        //mLeituraList = new ArrayList<Leitura>();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void doBindings() {
        super.onStart();
        super.observeError(mViewModelLeitura);
        observeGetAll();
        observeSucess();
    }

    private void bindCampos(List<Leitura> resultList){

        Double valorPremiacaoResumo = 0.0;
        Double valorRetiradoResumo = 0.0;
        long qtdPremiacaoResumo = 0;
        int valorQuantidadeVendidaResumo = 0;

        for(Leitura leitura: resultList){
            qtdPremiacaoResumo = qtdPremiacaoResumo + leitura.getQtdPremiacao();
            valorPremiacaoResumo = valorPremiacaoResumo + leitura.getTotalPremiado();
            valorQuantidadeVendidaResumo = valorQuantidadeVendidaResumo + leitura.quantidadeVendida;
            valorRetiradoResumo = valorRetiradoResumo + leitura.getValorRetiradoDouble();
        }

        mValorQuantidadeVendidaResumo.setText(String.valueOf(valorQuantidadeVendidaResumo));
        mValorPremiacaoResumo.setText(qtdPremiacaoResumo + "/ R$ "+ valorPremiacaoResumo.toString());
        mValorRetiradoResumo.setText("R$ " + valorRetiradoResumo);
    }

    private void observeGetAll(){
        mViewModelLeitura.mList.observe(this, new Observer<List<Leitura>>() {
            @Override
            public void onChanged(List<Leitura> resultList) {
                prepareRecyclerView(resultList);
                bindCampos(resultList);
            }
        });
    }

    private void observeSucess(){
        mViewModelLeitura.sucess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void prepareRecyclerView(List<Leitura> leituras){
        mAdapter = new RelatorioAdapter(leituras);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        hideProgressDialog();
    }

}
