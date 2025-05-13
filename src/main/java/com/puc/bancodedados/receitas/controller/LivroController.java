package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.LivroRequestDTO;
import com.puc.bancodedados.receitas.dtos.LivroResponseDTO;
import com.puc.bancodedados.receitas.services.LivroService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);
    private final LivroService livroService;

    @Autowired
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> criarLivro(@Valid @RequestBody LivroRequestDTO requestDTO) {
        logger.info("Requisição para criar livro: {}", requestDTO);
        LivroResponseDTO novoLivro = livroService.criarLivro(requestDTO);
        logger.info("Livro criado com sucesso: {}", novoLivro);
        return new ResponseEntity<>(novoLivro, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarLivros() {
        logger.info("Requisição para listar todos os livros");
        List<LivroResponseDTO> livros = livroService.listarLivros();
        logger.info("Listagem de livros retornada com {} itens", livros.size());
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<LivroResponseDTO> buscarLivroPorIsbn(@PathVariable String isbn) {
        logger.info("Requisição para buscar livro com ISBN: {}", isbn);
        LivroResponseDTO livro = livroService.buscarLivroPorIsbn(isbn);
        logger.info("Livro encontrado: {}", livro);
        return ResponseEntity.ok(livro);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<LivroResponseDTO> atualizarLivro(@PathVariable String isbn, @Valid @RequestBody LivroRequestDTO requestDTO) {
        logger.info("Requisição para atualizar livro com ISBN: {} com dados: {}", isbn, requestDTO);
        LivroResponseDTO livroAtualizado = livroService.atualizarLivro(isbn, requestDTO);
        logger.info("Livro atualizado com sucesso: {}", livroAtualizado);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deletarLivro(@PathVariable String isbn) {
        logger.info("Requisição para deletar livro com ISBN: {}", isbn);
        livroService.deletarLivro(isbn);
        logger.info("Livro com ISBN: {} deletado com sucesso", isbn);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{isbn}/receitas/{receitaId}")
    public ResponseEntity<LivroResponseDTO> adicionarReceitaAoLivro(@PathVariable String isbn, @PathVariable Long receitaId) {
        logger.info("Requisição para adicionar receita ID: {} ao livro ISBN: {}", receitaId, isbn);
        LivroResponseDTO livroAtualizado = livroService.adicionarReceitaAoLivro(isbn, receitaId);
        logger.info("Receita adicionada ao livro com sucesso: {}", livroAtualizado);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{isbn}/receitas/{receitaId}")
    public ResponseEntity<LivroResponseDTO> removerReceitaDoLivro(@PathVariable String isbn, @PathVariable Long receitaId) {
        logger.info("Requisição para remover receita ID: {} do livro ISBN: {}", receitaId, isbn);
        LivroResponseDTO livroAtualizado = livroService.removerReceitaDoLivro(isbn, receitaId);
        logger.info("Receita removida do livro com sucesso: {}", livroAtualizado);
        return ResponseEntity.ok(livroAtualizado);
    }
}