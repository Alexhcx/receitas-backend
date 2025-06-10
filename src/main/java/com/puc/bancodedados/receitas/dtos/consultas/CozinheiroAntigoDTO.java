package com.puc.bancodedados.receitas.dtos.consultas;

import java.time.LocalDate;
import java.util.List;

public record CozinheiroAntigoDTO(
    String nomeCozinheiro,
    LocalDate dataContrato,
    List<String> restaurantes,
    List<ReceitaPublicadaDTO> receitasCriadas
) {}