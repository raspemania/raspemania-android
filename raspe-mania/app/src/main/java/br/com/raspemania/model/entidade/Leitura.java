package br.com.raspemania.model.entidade;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

import br.com.raspemania.model.BaseModel;

@IgnoreExtraProperties
public class Leitura extends BaseModel implements Serializable {


    public Cliente cliente;
    public Produto produto;
    public int quantidadeVendida;
    public List<PremiacaoList> premiacaoList;
    public int quantidadeReposicao;

    //percentual de comissão do cliente no momento da leitura
    public Double porcentagemClienteLeitura;
    //valor do produto no momento da leitura
    public Double valorProdutoLeitura;


    @Exclude
    public String getPremiacao() {
        long qtd = 0;
        Double valor = 0.0;

        for(PremiacaoList premiacao : this.premiacaoList){
            qtd = qtd + premiacao.quantidadePremiada;
            valor = valor + (premiacao.valorPremiado*premiacao.quantidadePremiada);
        }
        return String.valueOf(qtd) + " /R$ " + String.valueOf(valor);
    }

    @Exclude
    public long getQtdPremiacao() {
        long qtd = 0;

        for(PremiacaoList premiacao : this.premiacaoList){
            qtd = qtd + premiacao.quantidadePremiada;
        }
        return qtd;
    }

    @Exclude
    public Double getTotalPremiado() {

        Double valor = 0.0;

        for(PremiacaoList premiacao : this.premiacaoList){
            valor = valor + (premiacao.valorPremiado*premiacao.quantidadePremiada);
        }
        return valor;
    }

    @Exclude
    public Double getValorRetiradoDouble() {
        int qtVendida = this.quantidadeVendida;
        Double valorProduto = this.valorProdutoLeitura;
        Double comissao = this.porcentagemClienteLeitura;
        Double totalPremiado = getTotalPremiado();

        Double valorRetirado = ((qtVendida*valorProduto)-((qtVendida*valorProduto)*(comissao/100D)))-totalPremiado;

        return valorRetirado;
    }

    @Exclude
    public String getValorRetirado() {
        int qtVendida = this.quantidadeVendida;
        Double valorProduto = this.produto.valor;
        Double comissao = this.porcentagemClienteLeitura;
        Double totalPremiado = getTotalPremiado();

        Double valorRetirado = ((qtVendida*valorProduto)-((qtVendida*valorProduto)*(comissao/100D)))-totalPremiado;

        return "R$ "+ String.valueOf(valorRetirado);
    }
}
