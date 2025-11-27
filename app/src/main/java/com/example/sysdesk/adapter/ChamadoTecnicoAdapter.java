package com.example.sysdesk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.activity.SugerirSolucaoForm;
import com.example.sysdesk.model.Chamado;

import java.util.ArrayList;
import java.util.List;

public class ChamadoTecnicoAdapter extends RecyclerView.Adapter<ChamadoTecnicoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onAceitarClick(Chamado chamado);
    }

    private Context context;
    private List<Chamado> chamados = new ArrayList<>();
    private OnItemClickListener listener;

    public ChamadoTecnicoAdapter(Context context, List<Chamado> chamados, OnItemClickListener listener) {
        this.context = context;
        this.chamados = chamados != null ? chamados : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chamado_tecnico, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chamado chamado = chamados.get(position);
        holder.txtTitulo.setText(chamado.getTitulo());
        holder.txtStatus.setText(chamado.getStatus());

        // Clique no item abre a tela de sugerir solução
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SugerirSolucaoForm.class);
            intent.putExtra("CHAMADO_ID", chamado.getId());
            context.startActivity(intent);

            // Também chama o listener se precisar de lógica adicional
            if (listener != null) listener.onAceitarClick(chamado);
        });
    }

    @Override
    public int getItemCount() {
        return chamados.size();
    }

    public void setChamados(List<Chamado> novosChamados) {
        this.chamados.clear();
        if (novosChamados != null) this.chamados.addAll(novosChamados);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
