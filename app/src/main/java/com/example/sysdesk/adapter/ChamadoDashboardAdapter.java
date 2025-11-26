package com.example.sysdesk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.model.Chamado;

import java.util.ArrayList;
import java.util.List;

public class ChamadoDashboardAdapter extends RecyclerView.Adapter<ChamadoDashboardAdapter.ViewHolder> {

    private final List<Chamado> listaOriginal;   // Lista completa
    private final List<Chamado> listaFiltrada;   // Lista que será exibida
    private final Context context;

    // Listener de clique
    public interface OnItemClickListener {
        void onItemClick(Chamado chamado);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ChamadoDashboardAdapter(Context context, List<Chamado> lista) {
        this.context = context;
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chamado, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chamado chamado = listaFiltrada.get(position);
        holder.txtTitulo.setText(chamado.getTitulo());
        holder.txtDescricao.setText(chamado.getDescricao());
        holder.txtStatus.setText(chamado.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(chamado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    // ==========================
    // ATUALIZA TODA A LISTA DE CHAMADOS
    // ==========================
    public void setChamados(List<Chamado> novosChamados) {
        listaOriginal.clear();
        listaOriginal.addAll(novosChamados);

        listaFiltrada.clear();
        listaFiltrada.addAll(novosChamados);

        notifyDataSetChanged();
    }

    // ==========================
    // FILTRA POR STATUS
    // ==========================
    public void filtrar(String status) {
        listaFiltrada.clear();

        if (status.equalsIgnoreCase("TODOS")) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (Chamado c : listaOriginal) {
                if (c.getStatus() != null && c.getStatus().equalsIgnoreCase(status)) {
                    listaFiltrada.add(c);
                }
            }
        }

        notifyDataSetChanged();
    }

    // ==========================
    // VIEWHOLDER
    // ==========================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescricao, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
