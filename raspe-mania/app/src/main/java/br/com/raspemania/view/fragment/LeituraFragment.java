package br.com.raspemania.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.helper.SharedPrefHelper;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.model.entidade.Leitura;
import br.com.raspemania.view.activity.LeituraActivity;
import br.com.raspemania.view.adapter.LeituraAdapter;
import br.com.raspemania.viewmodel.LeituraViewModel;

public class LeituraFragment extends BaseFragment {

    private Context context = getContext();
    private LeituraViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private LeituraAdapter mAdapter;
    private AppCompatTextView mSemLeitura;
    private AppCompatButton mNovoBtn;


    public static LeituraFragment newInstance() {
        return new LeituraFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leitura, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        context = getContext();
        mViewModel = ViewModelProviders.of(this).get(LeituraViewModel.class);
        doBindings();

        mNovoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, LeituraActivity.class));
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNovoBtn = view.findViewById(R.id.btn_novo);
        mSemLeitura = (AppCompatTextView) view.findViewById(R.id.semLeitura);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLista();
    }

    private void refreshLista() {

        Colaborador mColaborador;
        mColaborador = SharedPrefHelper.getSharedOBJECT(getContext(), ConstantHelper.COLABORADOR_PREF, Colaborador.class);

        if (mColaborador.perfil == ConstantHelper.PERFIL_ADM) {
            mViewModel.getAllLastDay();
        } else {
            mViewModel.getAllSpinnerForUser();
        }
    }

    private void doBindings() {
        super.onStart();
        super.observeError(mViewModel);
        observeSucess();
        observeGetAll();
    }

    private void observeGetAll() {
        mViewModel.mList.observe(this, new Observer<List<Leitura>>() {
            @Override
            public void onChanged(List<Leitura> resultList) {
                if(resultList.isEmpty()){
                    mSemLeitura.setVisibility(View.VISIBLE);
                } else {
                    mSemLeitura.setVisibility(View.INVISIBLE);
                }
                prepareRecyclerView(resultList);
            }
        });
    }

    private void observeSucess() {
        mViewModel.sucess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareRecyclerView(List<Leitura> leituras) {
        mAdapter = new LeituraAdapter(leituras, mViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        hideProgressDialog();
    }
}
