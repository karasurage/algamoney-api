package com.algaworks.algamoneyapi.repository.lancamento;

import com.algaworks.algamoneyapi.model.Categoria_;
import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.model.Lancamento_;
import com.algaworks.algamoneyapi.model.Pessoa_;
import com.algaworks.algamoneyapi.repository.filter.LancamentoFilter;
import com.algaworks.algamoneyapi.repository.projection.ResumoLancamento;
import com.algaworks.algamoneyapi.repository.util.PaginacaoUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        PaginacaoUtil.adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoLancamento> criteriaQuery = criteriaBuilder.createQuery(ResumoLancamento.class);
        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

        criteriaQuery.select(criteriaBuilder.construct(ResumoLancamento.class,
                root.get(Lancamento_.codigo), root.get(Lancamento_.descricao),
                root.get(Lancamento_.dataVencimento), root.get(Lancamento_.dataPagamento),
                root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
                root.get(Lancamento_.categoria).get(Categoria_.nome),
                root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

        Predicate[] predicates = criarRestricoes(lancamentoFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        TypedQuery<ResumoLancamento> query = manager.createQuery(criteriaQuery);

        PaginacaoUtil.adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
                                        Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
            predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
                    "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }

        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
                    lancamentoFilter.getDataVencimentoDe()));
        }

        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
                    lancamentoFilter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        criteriaQuery.select(criteriaBuilder.count(root));
        return manager.createQuery(criteriaQuery).getSingleResult();
    }
}