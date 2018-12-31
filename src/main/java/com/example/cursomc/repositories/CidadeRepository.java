package com.example.cursomc.repositories;

import com.example.cursomc.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, CriteriaBuilder.In> {
}
