package br.com.raspemania.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.raspemania.R;
import br.com.raspemania.helper.ConstantHelper;
import br.com.raspemania.model.entidade.Rota;
import br.com.raspemania.view.activity.RotaActivity;
import br.com.raspemania.viewmodel.RotaViewModel;

public class RotaAdapter extends RecyclerView.Adapter<RotaAdapter.RotaViewHolder> {

    public static String TAG = "RotaAdapter";

    private List<Rota> listRota;
    private Context context;
    private RotaViewModel mViewmodel;


    public RotaAdapter(List<Rota> listRota, RotaViewModel viewmodel) {
        this.listRota = listRota;
        this.mViewmodel = viewmodel;
    }

    @Override
    public RotaAdapter.RotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_rota, parent, false);
        RotaAdapter.RotaViewHolder viewHolder = new RotaAdapter.RotaViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RotaAdapter.RotaViewHolder holder, int position) {

        try {
            final Rota mItem = listRota.get(position);
            holder.nome.setText(mItem.nome);
            holder.colaborador.setText(mItem.colaborador.apelido);

            if(mItem.status == ConstantHelper.ATIVO) {
                holder.status_ativo.setVisibility(View.VISIBLE);
                holder.status_inativo.setVisibility(View.INVISIBLE);
            } else {
                holder.status_ativo.setVisibility(View.INVISIBLE);
                holder.status_inativo.setVisibility(View.VISIBLE);
            }

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RotaActivity.class);
                    intent.putExtra(TAG, mItem);
                    context.startActivity(intent);
                }
            });

            holder.deleteRota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewmodel.delete(mItem);
                    //listRota.remove(mItem);
                    notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        return listRota.size();
    }

    public static class RotaViewHolder extends RecyclerView.ViewHolder {
        public TextView nome;
        public TextView colaborador;
        public ConstraintLayout constraintLayout;
        public AppCompatImageButton deleteRota;
        public AppCompatImageView status_inativo;
        public AppCompatImageView status_ativo;

        public RotaViewHolder(View itemView) {
            super(itemView);
            this.nome = (TextView) itemView.findViewById(R.id.nome_rota);
            this.colaborador = (TextView) itemView.findViewById(R.id.apelido_colaborador);
            this.constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.layout_item_rota);
            this.deleteRota = (AppCompatImageButton) itemView.findViewById(R.id.delete_rota);
            this.status_inativo = (AppCompatImageView) itemView.findViewById(R.id.status_inativo);
            this.status_ativo = (AppCompatImageView) itemView.findViewById(R.id.status_ativo);
        }
    }
}
