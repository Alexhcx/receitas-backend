package com.puc.bancodedados.receitas.dtos;

import java.time.LocalDate;

public record CozinheiroResponseDTO(
        Long cozinheiroRg,
        String nomeEmpregado,
        String nomeFantasia,
        LocalDate dtContrato,
        Integer metaMensalReceitas,
        Integer prazoInicialDias
) {}
