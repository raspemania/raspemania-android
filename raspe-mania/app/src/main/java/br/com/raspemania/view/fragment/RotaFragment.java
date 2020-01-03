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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.model.entidade.Rota;
import br.com.raspemania.view.activity.RotaActivity;
import br.com.raspemania.view.adapter.RotaAdapter;
import br.com.raspemania.viewmodel.RotaViewModel;

public class RotaFragment extends BaseFragment {

    private Context context = getContext();
    private RotaViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RotaAdapter mAdapter;

    private AppCompatButton mNovoBtn;


    public static RotaFragment newInstance() {
        return new RotaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rota, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        context = getContext();
        mViewModel = ViewModelProviders.of(this).get(RotaViewModel.class);
        doBindings();
        mViewModel.getAll();

        mNovoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RotaActivity.class));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNovoBtn = view.findViewById(R.id.btn_novo);

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
        mViewModel.getAll();
    }

    private void doBindings(){
        super.onStart();
        super.observeError(mViewModel);
        observeSucess();

        observeGetAll();
    }

    private void observeGetAll(){
        mViewModel.mList.observe(this, new Observer<List<Rota>>() {
            @Override
            public void onChanged(List<Rota> resultList) {
                prepareRecyclerView(resultList);
            }
        });
    }

    private void observeSucess(){
        mViewModel.sucess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void prepareRecyclerView(List<Rota> rotas){
        mAdapter = new RotaAdapter(rotas, mViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        hideProgressDialog();
    }
}
