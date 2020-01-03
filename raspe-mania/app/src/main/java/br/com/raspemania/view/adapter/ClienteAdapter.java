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
import br.com.raspemania.model.entidade.Cliente;
import br.com.raspemania.view.activity.ClienteActivity;
import br.com.raspemania.viewmodel.ClienteViewModel;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {


    public static String TAG = "ClienteAdapter";

    private List<Cliente> listCliente;
    private Context context;
    private ClienteViewModel mViewmodel;


    public ClienteAdapter(List<Cliente> listCliente, ClienteViewModel viewmodel) {
        this.listCliente = listCliente;
        this.mViewmodel = viewmodel;
    }

    @Override
    public ClienteAdapter.ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_cliente, parent, false);
        ClienteAdapter.ClienteViewHolder viewHolder = new ClienteAdapter.ClienteViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ClienteAdapter.ClienteViewHolder holder, int position) {

        final Cliente mItem = listCliente.get(position);
        holder.codigo.setText(mItem.codigo);
        holder.rota.setText(mItem.rota.nome);
        holder.endereco.setText(mItem.endereco);
        holder.nomeCliente.setText(mItem.nome);

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
                Intent intent = new Intent(context, ClienteActivity.class);
                intent.putExtra(TAG, mItem);
                context.startActivity(intent);
            }
        });

        holder.deleteCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewmodel.delete(mItem);
                //listCliente.remove(mItem);
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
        return listCliente.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        public TextView codigo;
        public TextView rota;
        public TextView endereco;
        public TextView nomeCliente;
        public ConstraintLayout constraintLayout;
        public AppCompatImageButton deleteCliente;
        public AppCompatImageView status_inativo;
        public AppCompatImageView status_ativo;

        public ClienteViewHolder(View itemView) {
            super(itemView);
            this.codigo = (TextView) itemView.findViewById(R.id.codigo_cliente);
            this.rota = (TextView) itemView.findViewById(R.id.nome_rota);
            this.endereco = (TextView) itemView.findViewById(R.id.endereco_rota);
            this.nomeCliente = (TextView) itemView.findViewById(R.id.nome_cliente);
            this.constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.layout_item_cliente);
            this.deleteCliente = (AppCompatImageButton) itemView.findViewById(R.id.delete_cliente_btn);
            this.status_inativo = (AppCompatImageView) itemView.findViewById(R.id.status_inativo);
            this.status_ativo = (AppCompatImageView) itemView.findViewById(R.id.status_ativo);
        }
    }
}
