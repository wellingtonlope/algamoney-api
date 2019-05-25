package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.service.PessoaService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    private PessoaRepository pessoaRepository;
    private ApplicationEventPublisher applicationEventPublisher;
    private PessoaService pessoaService;

    public PessoaResource(PessoaRepository pessoaRepository, ApplicationEventPublisher applicationEventPublisher,
                          PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        applicationEventPublisher.publishEvent(new RecursoCriadoEvent(this, response,
            pessoaSalva.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);

        if (pessoa.isPresent())
            return ResponseEntity.ok(pessoa.get());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Pessoa> remover(@PathVariable Long codigo) {
        pessoaRepository.deleteById(codigo);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
        return ResponseEntity.ok(pessoaSalva);
    }

}
