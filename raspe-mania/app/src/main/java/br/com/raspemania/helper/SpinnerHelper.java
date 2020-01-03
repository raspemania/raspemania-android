package br.com.raspemania.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.model.entidade.Cliente;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.model.entidade.Produto;
import br.com.raspemania.model.entidade.Rota;

public class SpinnerHelper {

    /*--------------------------------------------------------------------------------------------*/
    public static final List<Cliente> spinnerCliente(List<Cliente> lista, Context context){

        List<Cliente> resultList = new ArrayList<>();
        Cliente p = new Cliente();
        p.codigo = context.getString(R.string.leitura_spinner_cliente);
        resultList.add(p);
        resultList.addAll(lista);

        return resultList;
    }

    /*--------------------------------------------------------------------------------------------*/
    public static final List<Produto> spinnerProduto(List<Produto> lista, Context context){

        List<Produto> resultList = new ArrayList<>();
        Produto p = new Produto();
        p.nome = context.getString(R.string.leitura_spinner_produto);
        resultList.add(p);
        resultList.addAll(lista);

        return resultList;
    }
    /*--------------------------------------------------------------------------------------------*/
    public static final List<Colaborador> spinnerColaborador(List<Colaborador> lista, Context context){

        List<Colaborador> resultList = new ArrayList<>();
        Colaborador p = new Colaborador();
        p.apelido = context.getString(R.string.spinner_colaborador);
        resultList.add(p);
        resultList.addAll(lista);

        return resultList;
    }
    /*--------------------------------------------------------------------------------------------*/

    public static final List<Rota> spinnerRota(List<Rota> lista, Context context){

        List<Rota> resultList = new ArrayList<>();
        Rota p = new Rota();
        p.nome = context.getString(R.string.spinner_rota);
        resultList.add(p);
        resultList.addAll(lista);

        return resultList;
    }
    /*--------------------------------------------------------------------------------------------*/
}
