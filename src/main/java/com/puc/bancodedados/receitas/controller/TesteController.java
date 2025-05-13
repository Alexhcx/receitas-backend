package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.TesteRequestDTO;
import com.puc.bancodedados.receitas.dtos.TesteResponseDTO;
import com.puc.bancodedados.receitas.services.TesteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testes")
public class TesteController {

    private static final Logger logger = LoggerFactory.getLogger(TesteController.class);
    private final TesteService testeService;

    @Autowired
    public TesteController(TesteService testeService) {
        this.testeService = testeService;
    }

    @PostMapping
    public ResponseEntity<TesteResponseDTO> criarTeste(@Valid @RequestBody TesteRequestDTO requestDTO) {
        logger.info("Requisição para criar teste: {}", requestDTO);
        TesteResponseDTO novoTeste = testeService.criarTeste(requestDTO);
        logger.info("Teste criado com sucesso: {}", novoTeste);
        return new ResponseEntity<>(novoTeste, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TesteResponseDTO>> listarTestes() {
        logger.info("Requisição para listar todos os testes");
        List<TesteResponseDTO> testes = testeService.listarTestes();
        logger.info("Listagem de testes retornada com {} itens", testes.size());
        return ResponseEntity.ok(testes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TesteResponseDTO> buscarTestePorId(@PathVariable Long id) {
        logger.info("Requisição para buscar teste com ID: {}", id);
        TesteResponseDTO teste = testeService.buscarTestePorId(id);
        logger.info("Teste encontrado: {}", teste);
        return ResponseEntity.ok(teste);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TesteResponseDTO> atualizarTeste(@PathVariable Long id, @Valid @RequestBody TesteRequestDTO requestDTO) {
        logger.info("Requisição para atualizar teste com ID: {} com dados: {}", id, requestDTO);
        TesteResponseDTO testeAtualizado = testeService.atualizarTeste(id, requestDTO);
        logger.info("Teste atualizado com sucesso: {}", testeAtualizado);
        return ResponseEntity.ok(testeAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTeste(@PathVariable Long id) {
        logger.info("Requisição para deletar teste com ID: {}", id);
        testeService.deletarTeste(id);
        logger.info("Teste com ID: {} deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}