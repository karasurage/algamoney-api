package com.algaworks.algamoneyapi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Table(name = "permissao")
@EqualsAndHashCode(of = {"codigo"})
public class Permissao {

    @Id
    private Long codigo;
    private String descricao;

}
