package com.puc.bancodedados.receitas.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record DegustadorRequestDTO(
                @NotNull(message = "RG do degustador (empregado) é obrigatório.") 
                Long degustadorRg,

                @NotNull(message = "Data do contrato é obrigatória.") 
                LocalDate dtContrato 
) {
}
