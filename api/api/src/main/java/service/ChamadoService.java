package com.sysdesk.api.service;

import com.sysdesk.api.model.Chamado;
import com.sysdesk.api.repository.ChamadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    public Chamado salvarChamado(Chamado chamado) {
        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    public Optional<Chamado> buscarPorId(Integer id) {
        return chamadoRepository.findById(Integer.valueOf(id));
    }

    public void deletarChamado(Integer id) {
        chamadoRepository.deleteById(Integer.valueOf(id));
    }
}
