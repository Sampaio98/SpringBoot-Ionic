package com.example.cursomc.repositories;

import com.example.cursomc.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface CidadeRepository extends JpaRepository<Cidade, CriteriaBuilder.In> {
}
