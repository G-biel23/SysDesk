package com.sysdesk.api.service;

import com.sysdesk.api.model.Categoria;
import com.sysdesk.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria salvarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(Integer.valueOf(id));
    }

    public void deletarCategoria(Integer id) {
        categoriaRepository.deleteById(Integer.valueOf(id));
    }
}
