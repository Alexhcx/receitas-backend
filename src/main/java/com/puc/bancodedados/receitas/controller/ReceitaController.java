package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.ReceitaRequestDTO;
import com.puc.bancodedados.receitas.dtos.ReceitaResponseDTO;
import com.puc.bancodedados.receitas.services.ReceitaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    private static final Logger logger = LoggerFactory.getLogger(ReceitaController.class);
    private final ReceitaService receitaService;

    @Autowired
    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @PostMapping
    public ResponseEntity<ReceitaResponseDTO> criarReceita(@Valid @RequestBody ReceitaRequestDTO requestDTO) {
        logger.info("Requisição para criar receita: {}", requestDTO);
        ReceitaResponseDTO novaReceita = receitaService.criarReceita(requestDTO);
        logger.info("Receita criada com sucesso: {}", novaReceita);
        return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReceitaResponseDTO>> listarReceitas() {
        logger.info("Requisição para listar todas as receitas");
        List<ReceitaResponseDTO> receitas = receitaService.listarReceitas();
        logger.info("Listagem de receitas retornada com {} itens", receitas.size());
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> buscarReceitaPorId(@PathVariable Long id) {
        logger.info("Requisição para buscar receita com ID: {}", id);
        ReceitaResponseDTO receita = receitaService.buscarReceitaPorId(id);
        logger.info("Receita encontrada: {}", receita);
        return ResponseEntity.ok(receita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> atualizarReceita(@PathVariable Long id, @Valid @RequestBody ReceitaRequestDTO requestDTO) {
        logger.info("Requisição para atualizar receita com ID: {} com dados: {}", id, requestDTO);
        ReceitaResponseDTO receitaAtualizada = receitaService.atualizarReceita(id, requestDTO);
        logger.info("Receita atualizada com sucesso: {}", receitaAtualizada);
        return ResponseEntity.ok(receitaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReceita(@PathVariable Long id) {
        logger.info("Requisição para deletar receita com ID: {}", id);
        receitaService.deletarReceita(id);
        logger.info("Receita com ID: {} deletada com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}