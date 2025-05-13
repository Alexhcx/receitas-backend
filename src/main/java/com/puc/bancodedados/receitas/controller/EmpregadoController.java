package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.EmpregadoRequestDTO;
import com.puc.bancodedados.receitas.dtos.EmpregadoResponseDTO;
import com.puc.bancodedados.receitas.services.EmpregadoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empregados")
public class EmpregadoController {

    private static final Logger logger = LoggerFactory.getLogger(EmpregadoController.class);
    private final EmpregadoService empregadoService;

    @Autowired
    public EmpregadoController(EmpregadoService empregadoService) {
        this.empregadoService = empregadoService;
    }

    @PostMapping
    public ResponseEntity<EmpregadoResponseDTO> criarEmpregado(@Valid @RequestBody EmpregadoRequestDTO requestDTO) {
        logger.info("Requisição para criar empregado: {}", requestDTO);
        EmpregadoResponseDTO novoEmpregado = empregadoService.criarEmpregado(requestDTO);
        logger.info("Empregado criado com sucesso: {}", novoEmpregado);
        return new ResponseEntity<>(novoEmpregado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmpregadoResponseDTO>> listarEmpregados() {
        logger.info("Requisição para listar todos os empregados");
        List<EmpregadoResponseDTO> empregados = empregadoService.listarEmpregados();
        logger.info("Listagem de empregados retornada com {} itens", empregados.size());
        return ResponseEntity.ok(empregados);
    }

    @GetMapping("/{rg}")
    public ResponseEntity<EmpregadoResponseDTO> buscarEmpregadoPorRg(@PathVariable Long rg) {
        logger.info("Requisição para buscar empregado com RG: {}", rg);
        EmpregadoResponseDTO empregado = empregadoService.buscarEmpregadoPorRg(rg);
        logger.info("Empregado encontrado: {}", empregado);
        return ResponseEntity.ok(empregado);
    }

    @PutMapping("/{rg}")
    public ResponseEntity<EmpregadoResponseDTO> atualizarEmpregado(@PathVariable Long rg, @Valid @RequestBody EmpregadoRequestDTO requestDTO) {
        logger.info("Requisição para atualizar empregado com RG: {} com dados: {}", rg, requestDTO);
        EmpregadoResponseDTO empregadoAtualizado = empregadoService.atualizarEmpregado(rg, requestDTO);
        logger.info("Empregado atualizado com sucesso: {}", empregadoAtualizado);
        return ResponseEntity.ok(empregadoAtualizado);
    }

    @DeleteMapping("/{rg}")
    public ResponseEntity<Void> deletarEmpregado(@PathVariable Long rg) {
        logger.info("Requisição para deletar empregado com RG: {}", rg);
        empregadoService.deletarEmpregado(rg);
        logger.info("Empregado com RG: {} deletado com sucesso", rg);
        return ResponseEntity.noContent().build();
    }
}