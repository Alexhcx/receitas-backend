package com.puc.bancodedados.receitas.dtos;

public record CozinheiroResponseDTO(
        Long cozinheiroRg,
        String nomeEmpregado,
        String nomeFantasia,
        Integer metaMensalReceitas,
        Integer prazoInicialDias
) {}
