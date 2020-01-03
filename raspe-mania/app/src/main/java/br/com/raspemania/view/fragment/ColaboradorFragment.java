package br.com.raspemania.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.view.adapter.ColaboradorAdapter;
import br.com.raspemania.viewmodel.ColaboradorViewModel;

public class ColaboradorFragment extends BaseFragment {

    private Context context = getContext();
    private ColaboradorViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private ColaboradorAdapter mAdapter;

    public static ColaboradorFragment newInstance() {
        return new ColaboradorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_colaborador, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getContext();
        mViewModel = ViewModelProviders.of(this).get(ColaboradorViewModel.class);
        doBindings();
        mViewModel.getAll();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        mViewModel.mList.observe(this, new Observer<List<Colaborador>>() {
            @Override
            public void onChanged(List<Colaborador> resultList) {
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

    private void prepareRecyclerView(List<Colaborador> colaboradores){
        mAdapter = new ColaboradorAdapter(colaboradores, mViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        hideProgressDialog();
    }
}
