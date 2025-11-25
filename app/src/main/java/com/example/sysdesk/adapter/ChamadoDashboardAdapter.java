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

    private List<Chamado> listaOriginal;
    private List<Chamado> listaFiltrada;
    private Context context;

    public ChamadoDashboardAdapter(Context context, List<Chamado> lista) {
        this.context = context;
        this.listaOriginal = lista;
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
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    // FILTRO PROFISSIONAL
    public void filtrar(String status) {
        listaFiltrada.clear();

        if (status.equals("TODOS")) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (Chamado c : listaOriginal) {
                if (c.getStatus().equalsIgnoreCase(status)) {
                    listaFiltrada.add(c);
                }
            }
        }

        notifyDataSetChanged();
    }

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
