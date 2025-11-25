package com.sysdesk.api.controller;

import com.sysdesk.api.model.Chamado;
import com.sysdesk.api.repository.ChamadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chamados")
@CrossOrigin(origins = "*") // permite acesso via Swagger e front-end
public class ChamadoController {

    @Autowired
    private ChamadoRepository chamadoRepository;

    // 🟢 Listar todos os chamados
    @GetMapping
    public List<Chamado> listar() {
        return chamadoRepository.findAll();
    }

    // 🟢 Buscar chamado por ID
    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarPorId(@PathVariable Integer id) {
        Optional<Chamado> chamado = chamadoRepository.findById(id);
        return chamado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🟢 Criar novo chamado
    @PostMapping
    public ResponseEntity<Chamado> criar(@RequestBody Chamado chamado) {
        chamado.setDataAbertura(java.time.LocalDateTime.now());
        Chamado novoChamado = chamadoRepository.save(chamado);
        return ResponseEntity.ok(novoChamado);
    }

    // 🟢 Atualizar chamado existente
    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizar(@PathVariable Integer id, @RequestBody Chamado chamadoAtualizado) {
        return chamadoRepository.findById(id)
                .map(chamado -> {
                    chamado.setTitulo(chamadoAtualizado.getTitulo());
                    chamado.setDescricao(chamadoAtualizado.getDescricao());
                    chamado.setStatus(chamadoAtualizado.getStatus());
                    chamado.setCategoria(chamadoAtualizado.getCategoria());
                    chamado.setUsuario(chamadoAtualizado.getUsuario());
                    chamado.setTecnico(chamadoAtualizado.getTecnico());
                    chamadoRepository.save(chamado);
                    return ResponseEntity.ok(chamado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🟢 Deletar chamado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (chamadoRepository.existsById(id)) {
            chamadoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
