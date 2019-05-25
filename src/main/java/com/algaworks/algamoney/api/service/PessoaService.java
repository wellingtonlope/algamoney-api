package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PessoaService {

    private PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
        Optional<Pessoa> pessoaSalva = pessoaRepository.findById(codigo);
        if(pessoaSalva.isEmpty())
            throw new EmptyResultDataAccessException(1);

        BeanUtils.copyProperties(pessoa, pessoaSalva.get(), "codigo");
        return pessoaRepository.save(pessoaSalva.get());
    }
}
