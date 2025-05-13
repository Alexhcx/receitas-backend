package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.CozinheiroRequestDTO;
import com.puc.bancodedados.receitas.dtos.CozinheiroResponseDTO;
import com.puc.bancodedados.receitas.services.CozinheiroService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cozinheiros")
public class CozinheiroController {

    private static final Logger logger = LoggerFactory.getLogger(CozinheiroController.class);
    private final CozinheiroService cozinheiroService;

    @Autowired
    public CozinheiroController(CozinheiroService cozinheiroService) {
        this.cozinheiroService = cozinheiroService;
    }

    @PostMapping
    public ResponseEntity<CozinheiroResponseDTO> criarCozinheiro(@Valid @RequestBody CozinheiroRequestDTO requestDTO) {
        logger.info("Requisição para criar cozinheiro: {}", requestDTO);
        CozinheiroResponseDTO novoCozinheiro = cozinheiroService.criarCozinheiro(requestDTO);
        logger.info("Cozinheiro criado com sucesso: {}", novoCozinheiro);
        return new ResponseEntity<>(novoCozinheiro, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CozinheiroResponseDTO>> listarCozinheiros() {
        logger.info("Requisição para listar todos os cozinheiros");
        List<CozinheiroResponseDTO> cozinheiros = cozinheiroService.listarCozinheiros();
        logger.info("Listagem de cozinheiros retornada com {} itens", cozinheiros.size());
        return ResponseEntity.ok(cozinheiros);
    }

    @GetMapping("/{rg}")
    public ResponseEntity<CozinheiroResponseDTO> buscarCozinheiroPorRg(@PathVariable Long rg) {
        logger.info("Requisição para buscar cozinheiro com RG: {}", rg);
        CozinheiroResponseDTO cozinheiro = cozinheiroService.buscarCozinheiroPorRg(rg);
        logger.info("Cozinheiro encontrado: {}", cozinheiro);
        return ResponseEntity.ok(cozinheiro);
    }

    @PutMapping("/{rg}")
    public ResponseEntity<CozinheiroResponseDTO> atualizarCozinheiro(@PathVariable Long rg, @Valid @RequestBody CozinheiroRequestDTO requestDTO) {
        logger.info("Requisição para atualizar cozinheiro com RG: {} com dados: {}", rg, requestDTO);
        CozinheiroResponseDTO cozinheiroAtualizado = cozinheiroService.atualizarCozinheiro(rg, requestDTO);
        logger.info("Cozinheiro atualizado com sucesso: {}", cozinheiroAtualizado);
        return ResponseEntity.ok(cozinheiroAtualizado);
    }

    @DeleteMapping("/{rg}")
    public ResponseEntity<Void> deletarCozinheiro(@PathVariable Long rg) {
        logger.info("Requisição para deletar cozinheiro com RG: {}", rg);
        cozinheiroService.deletarCozinheiro(rg);
        logger.info("Cozinheiro com RG: {} deletado com sucesso", rg);
        return ResponseEntity.noContent().build();
    }
}