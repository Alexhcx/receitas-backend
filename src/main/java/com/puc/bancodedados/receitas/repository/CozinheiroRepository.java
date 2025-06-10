package com.puc.bancodedados.receitas.repository;

import com.puc.bancodedados.receitas.model.Cozinheiro;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CozinheiroRepository extends JpaRepository<Cozinheiro, Long> {

  // FASE 2: Encontra todos os cozinheiros com uma data de contrato específica
  List<Cozinheiro> findAllByDtContrato(LocalDate dtContrato);

  // FASE 2: Encontra o primeiro cozinheiro ordenado pela data de contrato
  Cozinheiro findFirstByOrderByDtContratoAsc();

}
