package br.com.raspemania.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import br.com.raspemania.model.entidade.Colaborador;
import br.com.raspemania.view.activity.ColaboradorActivity;
import br.com.raspemania.viewmodel.ColaboradorViewModel;


public class ColaboradorAdapter extends RecyclerView.Adapter<ColaboradorAdapter.ColaboradorViewHolder>{

    public static String TAG = "ColaboradorAdapter";

    private List<Colaborador> listColaborador;
    private Context context;
    private ColaboradorViewModel mViewmodel;

    public ColaboradorAdapter(List<Colaborador> listColaborador, ColaboradorViewModel viewmodel) {
        this.listColaborador = listColaborador;
        this.mViewmodel = viewmodel;
    }
    @Override
    public ColaboradorAdapter.ColaboradorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_colaborador, parent, false);
        ColaboradorAdapter.ColaboradorViewHolder viewHolder = new ColaboradorAdapter.ColaboradorViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ColaboradorAdapter.ColaboradorViewHolder holder, int position) {

        final Colaborador mItem = listColaborador.get(position);
        holder.nomeColaborador.setText(mItem.email);
        holder.apelidoColaborador.setText(mItem.apelido);

        if(mItem.perfil == ConstantHelper.PERFIL_ADM){
            holder.deleteColaborador.setVisibility(View.INVISIBLE);
            holder.apelidoColaborador.setTextColor(Color.BLACK);
            holder.nomeColaborador.setTextColor(Color.BLACK);
        }

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
                Intent intent = new Intent(context, ColaboradorActivity.class);
                intent.putExtra(TAG, mItem);
                context.startActivity(intent);
            }
        });

        holder.deleteColaborador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewmodel.delete(mItem);
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
        return listColaborador.size();
    }

    public static class ColaboradorViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeColaborador;
        public TextView apelidoColaborador;
        public ConstraintLayout constraintLayout;
        public AppCompatImageButton deleteColaborador;
        public AppCompatImageView status_inativo;
        public AppCompatImageView status_ativo;

        public ColaboradorViewHolder(View itemView) {
            super(itemView);
            this.nomeColaborador = (TextView) itemView.findViewById(R.id.email_colaborador);
            this.apelidoColaborador = (TextView) itemView.findViewById(R.id.apelido_colaborador);
            this.constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.layout_colaborador);
            this.status_inativo = (AppCompatImageView) itemView.findViewById(R.id.status_inativo);
            this.status_ativo = (AppCompatImageView) itemView.findViewById(R.id.status_ativo);
            this.deleteColaborador = (AppCompatImageButton) itemView.findViewById(R.id.delete_colaborador_btn);
        }
    }
}