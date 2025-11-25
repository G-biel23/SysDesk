package com.example.sysdesk.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.model.Mensagem;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final List<Mensagem> listaMensagens;

    public ChatAdapter(List<Mensagem> lista) {
        this.listaMensagens = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensagem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensagem msg = listaMensagens.get(position);
        holder.txtMensagem.setText(msg.getTexto());

        // Alinha a mensagem à direita se for do usuário, esquerda se for técnico
        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) holder.container.getLayoutParams();
        if (msg.isUsuario()) {
            holder.txtMensagem.setBackgroundResource(R.drawable.bg_mensagem_usuario);
            params.gravity = Gravity.END;
        } else {
            holder.txtMensagem.setBackgroundResource(R.drawable.bg_mensagem_tecnico);
            params.gravity = Gravity.START;
        }
        holder.container.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return listaMensagens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensagem;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensagem = itemView.findViewById(R.id.txtMensagem);
            container = itemView.findViewById(R.id.containerMensagem);
        }
    }
}
