package com.puc.bancodedados.receitas.repository;

import com.puc.bancodedados.receitas.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Optional<Receita> findByNomeReceitaAndCozinheiroCozinheiroRg(String nomeReceita, Long cozinheiroRg);
    List<Receita> findByCategoriaId(Long categoriaId);
    List<Receita> findByCozinheiroCozinheiroRg(Long cozinheiroRg);

    //FASE 2
    List<Receita> findByDataCriacaoBetween(LocalDate inicio, LocalDate fim);
}
