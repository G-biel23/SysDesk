package com.example.sysdesk;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.adapter.ChatAdapter;
import com.example.sysdesk.model.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class ChatForm extends AppCompatActivity {

    private RecyclerView recyclerMensagens;
    private ChatAdapter adapter;
    private List<Mensagem> listaMensagens;

    private EditText editMensagem;
    private ImageButton btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_form);

        // RecyclerView
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        recyclerMensagens.setLayoutManager(new LinearLayoutManager(this));

        listaMensagens = new ArrayList<>();
        adapter = new ChatAdapter(listaMensagens);
        recyclerMensagens.setAdapter(adapter);

        // Inputs
        editMensagem = findViewById(R.id.editMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);

        // Evita que o botão feche a Activity
        btnEnviar.setFocusable(true);
        btnEnviar.setFocusableInTouchMode(true);

        // Enviar mensagem ao clicar no botão
        btnEnviar.setOnClickListener(v -> enviarMensagem());
    }

    private void enviarMensagem() {
        String texto = editMensagem.getText().toString().trim();
        if (!TextUtils.isEmpty(texto)) {
            // Mensagem do usuário
            Mensagem msgUsuario = new Mensagem(texto, true);
            listaMensagens.add(msgUsuario);
            adapter.notifyItemInserted(listaMensagens.size() - 1);
            recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);

            editMensagem.setText("");

            // Resposta simulada do técnico
            responderMensagem("Recebido: " + texto);
        }
    }

    private void responderMensagem(String texto) {
        Mensagem msgTecnico = new Mensagem(texto, false);
        listaMensagens.add(msgTecnico);
        adapter.notifyItemInserted(listaMensagens.size() - 1);
        recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
    }
}
