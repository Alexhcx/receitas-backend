package com.puc.bancodedados.receitas.dtos.consultas;

import java.time.LocalDate;
import java.util.List;

public record ReceitaPublicadaDTO(
    String categoriaDaReceita,
    String nomeDaReceita,
    LocalDate dataDaCriacao,
    List<String> titulosDosLivros
) {}
