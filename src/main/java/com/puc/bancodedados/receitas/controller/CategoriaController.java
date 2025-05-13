package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.CategoriaRequestDTO;
import com.puc.bancodedados.receitas.dtos.CategoriaResponseDTO;
import com.puc.bancodedados.receitas.services.CategoriaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@Valid @RequestBody CategoriaRequestDTO categoriaDTO) {
        logger.info("Requisição para criar categoria: {}", categoriaDTO);
        CategoriaResponseDTO novaCategoria = categoriaService.criarCategoria(categoriaDTO);
        logger.info("Categoria criada com sucesso: {}", novaCategoria);
        return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        logger.info("Requisição para listar todas as categorias");
        List<CategoriaResponseDTO> categorias = categoriaService.listarCategorias();
        logger.info("Listagem de categorias retornada com {} itens", categorias.size());
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarCategoriaPorId(@PathVariable Long id) {
        logger.info("Requisição para buscar categoria com ID: {}", id);
        CategoriaResponseDTO categoria = categoriaService.buscarCategoriaPorId(id);
        logger.info("Categoria encontrada: {}", categoria);
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO categoriaDTO) {
        logger.info("Requisição para atualizar categoria com ID: {} com dados: {}", id, categoriaDTO);
        CategoriaResponseDTO categoriaAtualizada = categoriaService.atualizarCategoria(id, categoriaDTO);
        logger.info("Categoria atualizada com sucesso: {}", categoriaAtualizada);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        logger.info("Requisição para deletar categoria com ID: {}", id);
        categoriaService.deletarCategoria(id);
        logger.info("Categoria com ID: {} deletada com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}