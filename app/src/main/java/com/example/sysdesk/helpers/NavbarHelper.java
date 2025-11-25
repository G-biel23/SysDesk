package com.example.sysdesk.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.sysdesk.R;
import com.example.sysdesk.activity.ClienteHomeForm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NavbarHelper {

    public static void configurarNavbar(Activity activity, Class<?> telaDoFab) {

        // ---------------- HISTÓRICO / HOME ----------------
        View btnHistorico = activity.findViewById(R.id.btnHistorico);
        if (btnHistorico != null) {
            btnHistorico.setOnClickListener(v -> {
                Intent i = new Intent(activity, ClienteHomeForm.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(i);
            });
        }

        // ---------------- CHAT (EM DESENVOLVIMENTO) ----------------
        View btnChat = activity.findViewById(R.id.btnChat);
        if (btnChat != null) {
            btnChat.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_em_desenvolvimento, null);
                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button btnOk = dialogView.findViewById(R.id.btnDialogOk);
                btnOk.setOnClickListener(view -> dialog.dismiss());

                dialog.show();
            });
        }

        // ---------------- NOTIFICAÇÕES ----------------
        View btnNotificacoes = activity.findViewById(R.id.btnNotificacoes);
        if (btnNotificacoes != null) {
            btnNotificacoes.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_em_desenvolvimento, null);
                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button btnOk = dialogView.findViewById(R.id.btnDialogOk);
                btnOk.setOnClickListener(view -> dialog.dismiss());

                dialog.show();
            });
        }

        // ---------------- CONFIGURAÇÕES ----------------
        View btnConfig = activity.findViewById(R.id.btnConfig);
        if (btnConfig != null) {
            btnConfig.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_em_desenvolvimento, null);
                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button btnOk = dialogView.findViewById(R.id.btnDialogOk);
                btnOk.setOnClickListener(view -> dialog.dismiss());

                dialog.show();
            });
        }

        // ---------------- FAB - ANIMAÇÃO + EVENTO ----------------
        FloatingActionButton fab = activity.findViewById(R.id.btnAbrirChamado);

        if (fab != null) {

            // Animação ao pressionar
            fab.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.fab_click_down));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.fab_click_up));
                        break;
                }
                return false;
            });

            // Abrir tela definida no parâmetro
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(activity, telaDoFab);
                activity.startActivity(intent);
            });
        }
    }
}
