package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    private LancamentoRepository lancamentoRepository;
    private ApplicationEventPublisher applicationEventPublisher;

    public LancamentoResource(LancamentoRepository lancamentoRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.lancamentoRepository = lancamentoRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping
    public ResponseEntity<List<Lancamento>> listar() {
        return ResponseEntity.ok(this.lancamentoRepository.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Lancamento> lancamento = this.lancamentoRepository.findById(codigo);

        if (lancamento.isPresent())
            return ResponseEntity.ok(lancamento.get());

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

        applicationEventPublisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }
}
