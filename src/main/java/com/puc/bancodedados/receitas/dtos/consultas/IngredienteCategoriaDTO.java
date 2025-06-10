package com.puc.bancodedados.receitas.dtos.consultas;

public record IngredienteCategoriaDTO(
    String nomeIngrediente,
    String nomeCategoria,
    long quantidadeDeReceitas
) {}
