package br.com.raspemania.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.model.entidade.PremiacaoList;

public class PremiacaoAdapter extends RecyclerView.Adapter<PremiacaoAdapter.PremiacaoViewHolder>{

    public static String TAG = "PremiacaoAdapter";

    private List<PremiacaoList> listPremiacao;
    private Context context;

    public PremiacaoAdapter(List<PremiacaoList> listPremiacao) {
        this.listPremiacao = listPremiacao;
    }
    @Override
    public PremiacaoAdapter.PremiacaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_premiacao, parent, false);
        PremiacaoAdapter.PremiacaoViewHolder viewHolder = new PremiacaoAdapter.PremiacaoViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PremiacaoAdapter.PremiacaoViewHolder holder, int position) {

        final PremiacaoList mItem = listPremiacao.get(position);
        holder.qtdPremiacaoList.setText(String.valueOf(mItem.quantidadePremiada));
        holder.valorPremiacao.setText(String.valueOf(mItem.valorPremiado));

        holder.delete_produto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPremiacao.remove(mItem);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        return listPremiacao.size();
    }

    public static class PremiacaoViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageButton delete_produto_btn;
        public AppCompatEditText valorPremiacao;
        public AppCompatEditText qtdPremiacaoList;

        public PremiacaoViewHolder(View itemView) {
            super(itemView);
            this.qtdPremiacaoList = (AppCompatEditText) itemView.findViewById(R.id.qtdPremiacaoList);
            this.valorPremiacao = (AppCompatEditText) itemView.findViewById(R.id.valorReposicao);
            this.delete_produto_btn = (AppCompatImageButton) itemView.findViewById(R.id.delete_produto_btn);
        }
    }
}