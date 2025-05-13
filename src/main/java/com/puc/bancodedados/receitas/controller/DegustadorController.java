package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.DegustadorRequestDTO;
import com.puc.bancodedados.receitas.dtos.DegustadorResponseDTO;
import com.puc.bancodedados.receitas.services.DegustadorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degustadores")
public class DegustadorController {

    private static final Logger logger = LoggerFactory.getLogger(DegustadorController.class);
    private final DegustadorService degustadorService;

    @Autowired
    public DegustadorController(DegustadorService degustadorService) {
        this.degustadorService = degustadorService;
    }

    @PostMapping
    public ResponseEntity<DegustadorResponseDTO> criarDegustador(@Valid @RequestBody DegustadorRequestDTO requestDTO) {
        logger.info("Requisição para criar degustador: {}", requestDTO);
        DegustadorResponseDTO novoDegustador = degustadorService.criarDegustador(requestDTO);
        logger.info("Degustador criado com sucesso: {}", novoDegustador);
        return new ResponseEntity<>(novoDegustador, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DegustadorResponseDTO>> listarDegustadores() {
        logger.info("Requisição para listar todos os degustadores");
        List<DegustadorResponseDTO> degustadores = degustadorService.listarDegustadores();
        logger.info("Listagem de degustadores retornada com {} itens", degustadores.size());
        return ResponseEntity.ok(degustadores);
    }

    @GetMapping("/{rg}")
    public ResponseEntity<DegustadorResponseDTO> buscarDegustadorPorRg(@PathVariable Long rg) {
        logger.info("Requisição para buscar degustador com RG: {}", rg);
        DegustadorResponseDTO degustador = degustadorService.buscarDegustadorPorRg(rg);
        logger.info("Degustador encontrado: {}", degustador);
        return ResponseEntity.ok(degustador);
    }

    @PutMapping("/{rg}")
    public ResponseEntity<DegustadorResponseDTO> atualizarDegustador(@PathVariable Long rg, @Valid @RequestBody DegustadorRequestDTO requestDTO) {
        logger.info("Requisição para atualizar degustador com RG: {} com dados: {}", rg, requestDTO);
        DegustadorResponseDTO degustadorAtualizado = degustadorService.atualizarDegustador(rg, requestDTO);
        logger.info("Degustador atualizado com sucesso: {}", degustadorAtualizado);
        return ResponseEntity.ok(degustadorAtualizado);
    }

    @DeleteMapping("/{rg}")
    public ResponseEntity<Void> deletarDegustador(@PathVariable Long rg) {
        logger.info("Requisição para deletar degustador com RG: {}", rg);
        degustadorService.deletarDegustador(rg);
        logger.info("Degustador com RG: {} deletado com sucesso", rg);
        return ResponseEntity.noContent().build();
    }
}