package com.puc.bancodedados.receitas.dtos;

import java.time.LocalDate; 

public record EditorResponseDTO(
        Long editorRg,
        String nomeEmpregado, 
        LocalDate dtContrato 
) {}
