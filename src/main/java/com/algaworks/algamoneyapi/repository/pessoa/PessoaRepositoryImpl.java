package com.algaworks.algamoneyapi.repository.pessoa;

import com.algaworks.algamoneyapi.model.Pessoa;
import com.algaworks.algamoneyapi.model.Pessoa_;
import com.algaworks.algamoneyapi.repository.filter.PessoaFilter;
import com.algaworks.algamoneyapi.repository.util.PaginacaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepositoryImpl implements PessoRepositoryQuery {

    @Autowired
    private EntityManager manager;

    @Override
    public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteriaQuery = criteriaBuilder.createQuery(Pessoa.class);

        Root<Pessoa> root = criteriaQuery.from(Pessoa.class);

        Predicate[] predicates = criarRestricoes(pessoaFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
        PaginacaoUtil.adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(pessoaFilter));
    }

    private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder criteriaBuilder, Root<Pessoa> root) {
        List<Predicate> predicates = new ArrayList<>();
        if(!StringUtils.isEmpty(pessoaFilter.getNome())){
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Pessoa_.nome)), "%"+pessoaFilter.getNome().toLowerCase()+"%"));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(PessoaFilter pessoaFilter) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Pessoa> root = criteriaQuery.from(Pessoa.class);

        Predicate[] predicates = criarRestricoes(pessoaFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        criteriaQuery.select(criteriaBuilder.count(root));
        return manager.createQuery(criteriaQuery).getSingleResult();
    }
}
