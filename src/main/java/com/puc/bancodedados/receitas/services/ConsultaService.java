package com.puc.bancodedados.receitas.services;

import com.puc.bancodedados.receitas.dtos.consultas.*;
import com.puc.bancodedados.receitas.model.*;
import com.puc.bancodedados.receitas.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

        @Autowired
        private ReceitaRepository receitaRepository;
        @Autowired
        private CozinheiroRepository cozinheiroRepository;
        @Autowired
        private IngredienteRepository ingredienteRepository;

        /**
         * Consulta 1: Encontra os cozinheiros campeões de receitas para um dado ano.
         */
        @Transactional(readOnly = true)
        public List<CozinheiroCampeaoDTO> getCozinheirosCampeoes(int ano) {
                LocalDate inicioDoAno = LocalDate.of(ano, Month.JANUARY, 1);
                LocalDate fimDoAno = LocalDate.of(ano, Month.DECEMBER, 31);

                List<Receita> receitasDoAno = receitaRepository.findByDataCriacaoBetween(inicioDoAno, fimDoAno);

                Map<Cozinheiro, Long> contagemPorCozinheiro = receitasDoAno.stream()
                                .collect(Collectors.groupingBy(Receita::getCozinheiro, Collectors.counting()));

                long maxReceitas = contagemPorCozinheiro.values().stream()
                                .max(Long::compare)
                                .orElse(0L);

                if (maxReceitas == 0) {
                        return List.of();
                }

                return contagemPorCozinheiro.entrySet().stream()
                                .filter(entry -> entry.getValue() == maxReceitas)
                                .map(entry -> new CozinheiroCampeaoDTO(
                                                entry.getKey().getEmpregado().getNomeEmpregado(),
                                                entry.getValue()))
                                .collect(Collectors.toList());
        }

        /**
         * Consulta 2: Encontra a quantidade de receitas com um ingrediente, agrupado
         * por categoria.
         */
        @Transactional(readOnly = true)
        public List<IngredienteCategoriaDTO> getReceitasPorIngrediente(Long ingredienteId) {
                Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Ingrediente não encontrado com ID: " + ingredienteId));

                List<Receita> receitasComIngrediente = ingrediente.getReceitaIngredientes().stream()
                                .map(ReceitaIngrediente::getReceita)
                                .collect(Collectors.toList());

                Map<Categoria, Long> contagemPorCategoria = receitasComIngrediente.stream()
                                .collect(Collectors.groupingBy(Receita::getCategoria, Collectors.counting()));

                return contagemPorCategoria.entrySet().stream()
                                .map(entry -> new IngredienteCategoriaDTO(
                                                ingrediente.getNomeIngrediente(),
                                                entry.getKey().getNomeCategoria(),
                                                entry.getValue()))
                                .collect(Collectors.toList());
        }

        /**
         * Consulta 3: Encontra o cozinheiro mais antigo e detalha suas receitas e
         * publicações.
         */
        @Transactional(readOnly = true)
        public List<CozinheiroAntigoDTO> getCozinheiroMaisAntigo() {
                Cozinheiro primeiroCozinheiro = cozinheiroRepository.findFirstByOrderByDtContratoAsc();
                if (primeiroCozinheiro == null) {
                        return List.of();
                }
                LocalDate dataMaisAntiga = primeiroCozinheiro.getDtContrato();

                List<Cozinheiro> cozinheirosMaisAntigos = cozinheiroRepository.findAllByDtContrato(dataMaisAntiga);

                return cozinheirosMaisAntigos.stream()
                                .map(this::mapToCozinheiroAntigoDTO)
                                .collect(Collectors.toList());
        }

        private CozinheiroAntigoDTO mapToCozinheiroAntigoDTO(Cozinheiro cozinheiro) {
                List<String> nomesRestaurantes = cozinheiro.getRestaurantes().stream()
                                .map(Restaurante::getNomeRestaurante)
                                .collect(Collectors.toList());

                List<ReceitaPublicadaDTO> receitasPublicadas = cozinheiro.getReceitas().stream()
                                .map(receita -> {
                                        List<String> titulosLivros = receita.getReceitaLivros().stream()
                                                        .map(receitaLivro -> receitaLivro.getLivro().getTitulo())
                                                        .collect(Collectors.toList());

                                        return new ReceitaPublicadaDTO(
                                                        receita.getCategoria().getNomeCategoria(),
                                                        receita.getNomeReceita(),
                                                        receita.getDataCriacao(),
                                                        titulosLivros);
                                })
                                .collect(Collectors.toList());

                return new CozinheiroAntigoDTO(
                                cozinheiro.getEmpregado().getNomeEmpregado(),
                                cozinheiro.getDtContrato(),
                                nomesRestaurantes,
                                receitasPublicadas);
        }
}
