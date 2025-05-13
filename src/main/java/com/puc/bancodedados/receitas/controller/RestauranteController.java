package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.RestauranteRequestDTO;
import com.puc.bancodedados.receitas.dtos.RestauranteResponseDTO;
import com.puc.bancodedados.receitas.services.RestauranteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

    private static final Logger logger = LoggerFactory.getLogger(RestauranteController.class);
    private final RestauranteService restauranteService;

    @Autowired
    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> criarRestaurante(@Valid @RequestBody RestauranteRequestDTO requestDTO) {
        logger.info("Requisição para criar restaurante: {}", requestDTO);
        RestauranteResponseDTO novoRestaurante = restauranteService.criarRestaurante(requestDTO);
        logger.info("Restaurante criado com sucesso: {}", novoRestaurante);
        return new ResponseEntity<>(novoRestaurante, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listarRestaurantes() {
        logger.info("Requisição para listar todos os restaurantes");
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarRestaurantes();
        logger.info("Listagem de restaurantes retornada com {} itens", restaurantes.size());
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarRestaurantePorId(@PathVariable Long id) {
        logger.info("Requisição para buscar restaurante com ID: {}", id);
        RestauranteResponseDTO restaurante = restauranteService.buscarRestaurantePorId(id);
        logger.info("Restaurante encontrado: {}", restaurante);
        return ResponseEntity.ok(restaurante);
    }

    @GetMapping("/cozinheiro/{cozinheiroRg}")
    public ResponseEntity<List<RestauranteResponseDTO>> listarRestaurantesPorCozinheiro(@PathVariable Long cozinheiroRg) {
        logger.info("Requisição para listar restaurantes do cozinheiro com RG: {}", cozinheiroRg);
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarRestaurantesPorCozinheiro(cozinheiroRg);
        logger.info("Listagem de restaurantes por cozinheiro retornada com {} itens", restaurantes.size());
        return ResponseEntity.ok(restaurantes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizarRestaurante(@PathVariable Long id, @Valid @RequestBody RestauranteRequestDTO requestDTO) {
        logger.info("Requisição para atualizar restaurante com ID: {} com dados: {}", id, requestDTO);
        RestauranteResponseDTO restauranteAtualizado = restauranteService.atualizarRestaurante(id, requestDTO);
        logger.info("Restaurante atualizado com sucesso: {}", restauranteAtualizado);
        return ResponseEntity.ok(restauranteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable Long id) {
        logger.info("Requisição para deletar restaurante com ID: {}", id);
        restauranteService.deletarRestaurante(id);
        logger.info("Restaurante com ID: {} deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}