package com.sysdesk.api.service;

import com.sysdesk.api.model.Usuario;
import com.sysdesk.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(Integer.valueOf(id));
    }

    public void deletarUsuario(Integer id) {
        usuarioRepository.deleteById(Integer.valueOf(id));
    }
}
