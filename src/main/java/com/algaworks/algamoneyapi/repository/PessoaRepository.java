package com.algaworks.algamoneyapi.repository;

import com.algaworks.algamoneyapi.repository.pessoa.PessoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algamoneyapi.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoRepositoryQuery {

}