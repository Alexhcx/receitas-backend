package com.puc.bancodedados.receitas.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate; 

public record EditorRequestDTO(
        @NotNull(message = "RG do editor (empregado) é obrigatório.")
        Long editorRg,

        @NotNull(message = "Data do contrato é obrigatória.") 
        LocalDate dtContrato 
) {}