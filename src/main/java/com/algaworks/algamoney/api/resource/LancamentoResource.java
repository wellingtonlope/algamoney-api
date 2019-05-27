package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    private LancamentoRepository lancamentoRepository;

    public LancamentoResource(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Lancamento>> listar() {
        return ResponseEntity.ok(this.lancamentoRepository.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Lancamento> lancamento = this.lancamentoRepository.findById(codigo);

        if(lancamento.isPresent())
            return ResponseEntity.ok(lancamento.get());

        return ResponseEntity.notFound().build();
    }
}
