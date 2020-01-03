package br.com.raspemania.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import br.com.raspemania.R;

public class ExampleFragment extends BaseFragment {

    public static ExampleFragment newInstance() {
        return new ExampleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //onde você constrói ou infla sua interface,
        // faz conexão com alguma fonte de dados e retorna à Activity pai
        // para poder integrá-lo em sua hierarquia de Views
        return inflater.inflate(R.layout.fragment_produto, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //Isso notifica nosso Fragment que a Activity pai completou seu ciclo no onCreate e é aqui
        // que podemos interagir com segurança com a interface de usuário.
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // textView = view.findViewById(R.id.text_example);

    }

    @Override
    public void onDestroyView() {
        //O onDestroyView é correspondente ao onDestroy da Activity e é chamado imediatamente antes do Fragment ser destruido.
        // Ele funciona independente da Activity pai.
        //Aqui é onde você deve limpar quaisquer recursos especificamente relacionados à interface,
        // como bitmaps na memória, cursores de dados, para garantir que não haja problemas de memória.
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        //O onAttach é onde podemos obter uma referência para a Activity pai
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        //O onDetach é a última coisa que acontece no ciclo de vida, mesmo após o seu Fragment ser tecnicamente destruído.
        super.onDetach();
    }

    @Override
    public void onStop() {
        //Uma vez que o Fragment não está mais visível, há uma chance dele ser encerrado.
        //Isso pode acontecer, após o onStop, no caso de a Activity ser encerrada, pois o Fragment
        // faz parte da sua hierarquia de Views ou após o onDestroyView
        super.onStop();
    }

}
