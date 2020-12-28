package com.algaworks.algamoneyapi.repository.pessoa;

import com.algaworks.algamoneyapi.model.Pessoa;
import com.algaworks.algamoneyapi.repository.filter.PessoaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PessoRepositoryQuery {

    Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);
}
