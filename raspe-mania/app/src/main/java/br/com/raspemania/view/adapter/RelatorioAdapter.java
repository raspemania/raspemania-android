package br.com.raspemania.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.model.entidade.Leitura;

public class RelatorioAdapter extends RecyclerView.Adapter<RelatorioAdapter.RelatorioViewHolder>{

    public static String TAG = "RelatorioAdapter";

    private List<Leitura> listRelatorio;
    //private Context context;

    public RelatorioAdapter(List<Leitura> listRelatorio) {
        this.listRelatorio = listRelatorio;
    }
    @Override
    public RelatorioAdapter.RelatorioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_relatorio, parent, false);
        RelatorioAdapter.RelatorioViewHolder viewHolder = new RelatorioAdapter.RelatorioViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RelatorioAdapter.RelatorioViewHolder holder, int position) {

        final Leitura mItem = listRelatorio.get(position);
        mItem.getTotalPremiado();
        holder.dataCadastro.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(mItem.dataUltimaAtualizacao));
        holder.colaboradorRelatorio.setText(mItem.cliente.rota.colaborador.apelido);
        holder.rotaRelatorio.setText(mItem.cliente.rota.nome);
        holder.codigoCliente.setText(mItem.cliente.codigo);
        holder.valorQuantidadeVendida.setText(String.valueOf(mItem.quantidadeVendida));
        holder.valorPremiacao.setText(mItem.getPremiacao());
        holder.valorRetirado.setText(mItem.getValorRetirado());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //context = recyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        return listRelatorio.size();
    }

    public static class RelatorioViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView dataCadastro;
        public AppCompatTextView colaboradorRelatorio;
        public AppCompatTextView rotaRelatorio;
        public AppCompatTextView codigoCliente;
        public AppCompatTextView valorQuantidadeVendida;
        public AppCompatTextView valorPremiacao;
        public AppCompatTextView valorRetirado;

        public RelatorioViewHolder(View itemView) {
            super(itemView);
            this.dataCadastro = (AppCompatTextView) itemView.findViewById(R.id.dataCadastro);
            this.colaboradorRelatorio = (AppCompatTextView) itemView.findViewById(R.id.colaboradorRelatorio);
            this.rotaRelatorio = (AppCompatTextView) itemView.findViewById(R.id.rotaRelatorio);
            this.codigoCliente = (AppCompatTextView) itemView.findViewById(R.id.codigoCliente);
            this.valorQuantidadeVendida = (AppCompatTextView) itemView.findViewById(R.id.valorQuantidadeVendida);
            this.valorPremiacao = (AppCompatTextView) itemView.findViewById(R.id.valorPremiacao);
            this.valorRetirado = (AppCompatTextView) itemView.findViewById(R.id.valorRetirado);

        }
    }
}