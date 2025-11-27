package com.example.sysdesk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.model.Sugestao;

import java.util.ArrayList;
import java.util.List;

public class SugestaoAdapter extends RecyclerView.Adapter<SugestaoAdapter.SugestaoViewHolder> {

    private List<Sugestao> sugestoes;

    public SugestaoAdapter(List<Sugestao> sugestoes) {
        this.sugestoes = sugestoes != null ? sugestoes : new ArrayList<>();
    }

    @NonNull
    @Override
    public SugestaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugestao, parent, false);
        return new SugestaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SugestaoViewHolder holder, int position) {
        Sugestao s = sugestoes.get(position);
        holder.texto.setText(s.getDescricao());
        holder.tecnico.setText(s.getTecnico().getNome());
        holder.dataHora.setText(s.getDataHora().toString());
    }

    @Override
    public int getItemCount() {
        return sugestoes.size();
    }

    public void setSugestoes(List<Sugestao> list) {
        sugestoes.clear();
        sugestoes.addAll(list);
        notifyDataSetChanged();
    }

    public void addSugestao(Sugestao s) {
        sugestoes.add(s);
        notifyItemInserted(sugestoes.size() - 1);
    }

    static class SugestaoViewHolder extends RecyclerView.ViewHolder {
        TextView texto, tecnico, dataHora;

        public SugestaoViewHolder(@NonNull View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.textSugestao);
            tecnico = itemView.findViewById(R.id.textTecnico);
            dataHora = itemView.findViewById(R.id.textDataHora);
        }
    }
}
