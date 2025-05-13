package com.puc.bancodedados.receitas.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CozinheiroRequestDTO(
        @NotNull(message = "RG do cozinheiro (empregado) é obrigatório.")
        Long cozinheiroRg,

        @Size(max = 80, message = "Nome fantasia não pode exceder 80 caracteres.")
        String nomeFantasia,

        @NotNull(message = "Data do contrato é obrigatória.") // Added validation
        LocalDate dtContrato, // Added dtContrato

        @NotNull(message = "Meta mensal de receitas é obrigatória.")
        @Min(value = 1, message = "Meta mensal de receitas deve ser no mínimo 1.")
        Integer metaMensalReceitas,

        @NotNull(message = "Prazo inicial em dias é obrigatório.")
        @Min(value = 0, message = "Prazo inicial não pode ser negativo.")
        @Max(value = 45, message = "Prazo inicial não pode exceder 45 dias.")
        Integer prazoInicialDias
) {}
