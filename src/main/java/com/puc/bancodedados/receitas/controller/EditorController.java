package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.EditorRequestDTO;
import com.puc.bancodedados.receitas.dtos.EditorResponseDTO;
import com.puc.bancodedados.receitas.services.EditorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editores")
public class EditorController {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);
    private final EditorService editorService;

    @Autowired
    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }

    @PostMapping
    public ResponseEntity<EditorResponseDTO> criarEditor(@Valid @RequestBody EditorRequestDTO requestDTO) {
        logger.info("Requisição para criar editor: {}", requestDTO);
        EditorResponseDTO novoEditor = editorService.criarEditor(requestDTO);
        logger.info("Editor criado com sucesso: {}", novoEditor);
        return new ResponseEntity<>(novoEditor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EditorResponseDTO>> listarEditores() {
        logger.info("Requisição para listar todos os editores");
        List<EditorResponseDTO> editores = editorService.listarEditores();
        logger.info("Listagem de editores retornada com {} itens", editores.size());
        return ResponseEntity.ok(editores);
    }

    @GetMapping("/{rg}")
    public ResponseEntity<EditorResponseDTO> buscarEditorPorRg(@PathVariable Long rg) {
        logger.info("Requisição para buscar editor com RG: {}", rg);
        EditorResponseDTO editor = editorService.buscarEditorPorRg(rg);
        logger.info("Editor encontrado: {}", editor);
        return ResponseEntity.ok(editor);
    }

    @PutMapping("/{rg}") 
    public ResponseEntity<EditorResponseDTO> atualizarEditor(@PathVariable Long rg, @Valid @RequestBody EditorRequestDTO requestDTO) {
        logger.info("Requisição para atualizar editor com RG: {} com dados: {}", rg, requestDTO);
        EditorResponseDTO editorAtualizado = editorService.atualizarEditor(rg, requestDTO);
        logger.info("Editor atualizado com sucesso: {}", editorAtualizado);
        return ResponseEntity.ok(editorAtualizado);
    }

    @DeleteMapping("/{rg}")
    public ResponseEntity<Void> deletarEditor(@PathVariable Long rg) {
        logger.info("Requisição para deletar editor com RG: {}", rg);
        editorService.deletarEditor(rg);
        logger.info("Editor com RG: {} deletado com sucesso", rg);
        return ResponseEntity.noContent().build();
    }
}