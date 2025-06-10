package com.puc.bancodedados.receitas.controller;

import com.puc.bancodedados.receitas.dtos.consultas.CozinheiroAntigoDTO;
import com.puc.bancodedados.receitas.dtos.consultas.CozinheiroCampeaoDTO;
import com.puc.bancodedados.receitas.dtos.consultas.IngredienteCategoriaDTO;
import com.puc.bancodedados.receitas.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    /**
     * Consulta 1: Cozinheiros campeões de receitas em um ano.
     * GET /api/consultas/cozinheiros-campeoes?ano=2024
     */
    @GetMapping("/cozinheiros-campeoes")
    public ResponseEntity<List<CozinheiroCampeaoDTO>> getCozinheirosCampeoes(@RequestParam int ano) {
        List<CozinheiroCampeaoDTO> resultado = consultaService.getCozinheirosCampeoes(ano);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Consulta 2: Quantidade de receitas com um ingrediente, por categoria.
     * GET /api/consultas/receitas-por-ingrediente?ingredienteId=1
     */
    @GetMapping("/receitas-por-ingrediente")
    public ResponseEntity<List<IngredienteCategoriaDTO>> getReceitasPorIngrediente(@RequestParam Long ingredienteId) {
        List<IngredienteCategoriaDTO> resultado = consultaService.getReceitasPorIngrediente(ingredienteId);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Consulta 3: Cozinheiro(s) mais antigo(s), suas receitas e publicações.
     *GET /api/consultas/cozinheiros-mais-antigos
     */
    @GetMapping("/cozinheiros-mais-antigos")
    public ResponseEntity<List<CozinheiroAntigoDTO>> getCozinheiroMaisAntigo() {
        List<CozinheiroAntigoDTO> resultado = consultaService.getCozinheiroMaisAntigo();
        return ResponseEntity.ok(resultado);
    }
}