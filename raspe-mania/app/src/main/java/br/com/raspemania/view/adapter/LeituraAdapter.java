package br.com.raspemania.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.helper.SharedPrefHelper;
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.model.entidade.Leitura;
import br.com.raspemania.viewmodel.LeituraViewModel;

public class LeituraAdapter extends RecyclerView.Adapter<LeituraAdapter.LeituraViewHolder>{

    public static String TAG = "LeituraAdapter";

    private List<Leitura> listLeitura;
    private Context context;
    private LeituraViewModel mViewmodel;

    public LeituraAdapter(List<Leitura> listLeitura, LeituraViewModel viewmodel) {
        this.listLeitura = listLeitura;
        this.mViewmodel = viewmodel;
    }
    @Override
    public LeituraAdapter.LeituraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem;
        listItem = layoutInflater.inflate(R.layout.item_leitura, parent, false);

        Colaborador mColaborador;
        mColaborador = SharedPrefHelper.getSharedOBJECT(context, ConstantHelper.COLABORADOR_PREF, Colaborador.class);

        if (mColaborador.perfil != ConstantHelper.PERFIL_ADM) {
            AppCompatImageButton deleteLeitura = listItem.findViewById(R.id.deleteLeitura);
            deleteLeitura.setVisibility(View.INVISIBLE);
        }

        LeituraAdapter.LeituraViewHolder viewHolder = new LeituraAdapter.LeituraViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LeituraAdapter.LeituraViewHolder holder, int position) {

        final Leitura mItem = listLeitura.get(position);
        holder.codigoCliente.setText(mItem.cliente.codigo);
        holder.dataCadastro.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(mItem.dataUltimaAtualizacao));
        holder.valorQuantidadeVendida.setText(String.valueOf(mItem.quantidadeVendida));
        holder.valorReposicao.setText(String.valueOf(mItem.quantidadeReposicao));
        holder.valorPremiacao.setText(mItem.getPremiacao());
        holder.valorRetirado.setText(mItem.getValorRetirado());

        holder.deleteLeitura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmaDelete(mItem);
            }
        });
    }

    public void confirmaDelete(final Leitura mItem) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Atenção");
        alertDialog.setMessage("Deseja excluir a leitura? Essa ação não poderá ser desfeita.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mViewmodel.delete(mItem);
                        listLeitura.remove(mItem);
                        notifyDataSetChanged();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        return listLeitura.size();
    }

    public static class LeituraViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView codigoCliente;
        public AppCompatTextView dataCadastro;
        public AppCompatImageButton deleteLeitura;
        public AppCompatTextView valorQuantidadeVendida;
        public AppCompatTextView valorReposicao;
        public AppCompatTextView valorPremiacao;
        public AppCompatTextView valorRetirado;

        public LeituraViewHolder(View itemView) {
            super(itemView);
            this.codigoCliente = (AppCompatTextView) itemView.findViewById(R.id.codigoCliente);
            this.dataCadastro = (AppCompatTextView) itemView.findViewById(R.id.dataCadastro);
            this.deleteLeitura = (AppCompatImageButton) itemView.findViewById(R.id.deleteLeitura);
            this.valorQuantidadeVendida = (AppCompatTextView) itemView.findViewById(R.id.valorQuantidadeVendida);
            this.valorReposicao = (AppCompatTextView) itemView.findViewById(R.id.valorReposicao);
            this.valorPremiacao = (AppCompatTextView) itemView.findViewById(R.id.valorPremiacao);
            this.valorRetirado = (AppCompatTextView) itemView.findViewById(R.id.valorRetirado);
        }
    }
}