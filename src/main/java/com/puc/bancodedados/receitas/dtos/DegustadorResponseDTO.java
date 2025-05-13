package com.puc.bancodedados.receitas.dtos;

import java.time.LocalDate; // Import LocalDate

public record DegustadorResponseDTO(
        Long degustadorRg,
        String nomeEmpregado, 
        LocalDate dtContrato 
) {}
