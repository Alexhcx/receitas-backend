package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.IngredienteRequestDTO;
import com.puc.bancodedados.receitas.dtos.IngredienteResponseDTO;
import com.puc.bancodedados.receitas.services.IngredienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    private static final Logger logger = LoggerFactory.getLogger(IngredienteController.class);
    private final IngredienteService ingredienteService;

    @Autowired
    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @PostMapping
    public ResponseEntity<IngredienteResponseDTO> criarIngrediente(@Valid @RequestBody IngredienteRequestDTO requestDTO) {
        logger.info("Requisição para criar ingrediente: {}", requestDTO);
        IngredienteResponseDTO novoIngrediente = ingredienteService.criarIngrediente(requestDTO);
        logger.info("Ingrediente criado com sucesso: {}", novoIngrediente);
        return new ResponseEntity<>(novoIngrediente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IngredienteResponseDTO>> listarIngredientes() {
        logger.info("Requisição para listar todos os ingredientes");
        List<IngredienteResponseDTO> ingredientes = ingredienteService.listarIngredientes();
        logger.info("Listagem de ingredientes retornada com {} itens", ingredientes.size());
        return ResponseEntity.ok(ingredientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteResponseDTO> buscarIngredientePorId(@PathVariable Long id) {
        logger.info("Requisição para buscar ingrediente com ID: {}", id);
        IngredienteResponseDTO ingrediente = ingredienteService.buscarIngredientePorId(id);
        logger.info("Ingrediente encontrado: {}", ingrediente);
        return ResponseEntity.ok(ingrediente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredienteResponseDTO> atualizarIngrediente(@PathVariable Long id, @Valid @RequestBody IngredienteRequestDTO requestDTO) {
        logger.info("Requisição para atualizar ingrediente com ID: {} com dados: {}", id, requestDTO);
        IngredienteResponseDTO ingredienteAtualizado = ingredienteService.atualizarIngrediente(id, requestDTO);
        logger.info("Ingrediente atualizado com sucesso: {}", ingredienteAtualizado);
        return ResponseEntity.ok(ingredienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarIngrediente(@PathVariable Long id) {
        logger.info("Requisição para deletar ingrediente com ID: {}", id);
        ingredienteService.deletarIngrediente(id);
        logger.info("Ingrediente com ID: {} deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}