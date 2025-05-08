package com.puc.bancodedados.receitas.dtos;

import jakarta.validation.constraints.NotNull;

public record EditorRequestDTO(
        @NotNull(message = "RG do editor (empregado) é obrigatório.")
        Long editorRg
) {}
